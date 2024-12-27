package com.personal.finance_tracker_service.splitwise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
public class SplitwiseService {

	@Value("${splitwise.api.url}")
	private String apiUrl;
	
	@Value("${splitwise.api.key}")
	private String apiKey;
	
	private final RestTemplate restTemplate;
	
	public SplitwiseService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String getSplitwise(String getValue) {
		String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
						.pathSegment(getValue)
						.toUriString();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
	}
	
	public List<GroupFriend> parseGroupsAndFriends(String groupResponseBody, String friendResponseBody) {
		List<GroupFriend> groupAndFriendList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode groupRootNode = objectMapper.readTree(groupResponseBody);
			JsonNode groupsNode = groupRootNode.path("groups");
			for (JsonNode groupNode : groupsNode) {
				GroupFriend groupfriend = new GroupFriend();
				groupfriend.setId(groupNode.path("id").asInt());
				groupfriend.setName(groupNode.path("name").asText());
				groupfriend.setType("group");
				List<GroupFriend> members = new ArrayList<>();
				for (JsonNode memberNode : groupNode.path("members")) {
					GroupFriend member = new GroupFriend();
					member.setId(memberNode.path("id").asInt());
					String firstname = memberNode.path("first_name").asText();
					String lastname = memberNode.path("last_name").asText("");
					member.setName(firstname + (lastname.isEmpty()? "" : " " + lastname));
					member.setType("friend");
					members.add(member);
				}
				groupfriend.setParticipants(members);
				groupAndFriendList.add(groupfriend);
			}
			JsonNode friendRootNode = objectMapper.readTree(friendResponseBody);
			JsonNode friendsNode = friendRootNode.path("friends");
			
			for (JsonNode friendNode : friendsNode) {
				GroupFriend groupfriend = new GroupFriend();
				String firstname = friendNode.path("first_name").asText();
				String lastname = friendNode.path("last_name").asText("");
				groupfriend.setId(friendNode.path("id").asInt());
				groupfriend.setName(firstname + (lastname.isEmpty()? "" : " " + lastname));
				groupfriend.setType("friend");
				groupAndFriendList.add(groupfriend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return groupAndFriendList;
	}
	
	public String createExpense(String expenseBody) {
		String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
		.pathSegment("create_expense")
		.toUriString();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.set("Content-Type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> requestBody = new HashMap<>();
		try 
		{
			System.out.println(expenseBody);
			int groupId = 0;
			JsonNode ExpenseNode = objectMapper.readTree(expenseBody);
			JsonNode participantNode = ExpenseNode.path("participants");
			for(JsonNode participant: participantNode) {
				if (participant.path("type").asText().equals("group"))
					groupId = participant.path("id").asInt();
			}
			double amount = ExpenseNode.path("amount").asDouble();
			boolean splitEqually = !(ExpenseNode.path("isEquallyClicked").asBoolean());
			boolean containedUser=false;
			
			requestBody.put("group_id", groupId);
			requestBody.put("description", ExpenseNode.path("description").asText(""));
			requestBody.put("cost", amount);
			requestBody.put("currency_code", "USD");
			
			if (splitEqually) {
				requestBody.put("split_equally", splitEqually);
				HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
				return response.getBody();
			} else {
				String activeSplitTab = ExpenseNode.path("activeSplitTab").asText("");
				if (activeSplitTab.equals("SplitEqually")) {
					JsonNode checkedListNode = ExpenseNode.path("checkedList");
					int checkLength = checkedListNode.size();
					double splitAmount = amount / checkLength;
					for(int i=0; i< checkLength; i++) {
						JsonNode item = checkedListNode.get(i);
						requestBody.put("users__"+ i +"__user_id", item.get("id").asInt());
						requestBody.put("users__"+ i +"__owed_share", splitAmount);
						if (item.get("id").asInt() == 22705915) {
							containedUser = true;
							requestBody.put("users__"+ i +"__paid_share", amount);
						} else {
							requestBody.put("users__"+ i +"__paid_share", 0);
						}
					}
					if (!containedUser) {
						requestBody.put("users__"+ checkLength +"__user_id", 22705915);
						requestBody.put("users__"+ checkLength +"__paid_share", amount);
						requestBody.put("users__"+ checkLength +"__owed_share", 0);
					}
				} else if (activeSplitTab.equals("SplitShares")) {
					JsonNode shareListNode = ExpenseNode.path("shareAmount");
					int shareLength = shareListNode.size();
					for(int i=0; i< shareLength; i++) {
						JsonNode item = shareListNode.get(i);
						requestBody.put("users__"+ i +"__user_id", item.get("id").asInt());
						requestBody.put("users__"+ i +"__owed_share", item.get("amount").asDouble());
						if (item.get("id").asInt() == 22705915) {
							requestBody.put("users__"+ i +"__paid_share", amount);
						} else {
							requestBody.put("users__"+ i +"__paid_share", 0);
						}
					}
				}
			}
			ObjectMapper objectMapper1 = new ObjectMapper();
			System.out.println(objectMapper1.writeValueAsString(requestBody));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		return response.getBody();
	}

}