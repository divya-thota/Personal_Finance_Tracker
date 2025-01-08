package com.personal.finance_tracker_service.splitwise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SplitwiseService {

	@Value("${splitwise.api.url}")
	private String apiUrl;
	
	@Value("${splitwise.api.key}")
	private String apiKey;

	private int currentUserId;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CURRENCY_CODE = "USD";

    @PostConstruct
    private void initializeCurrentUserId() {
        try {
            this.currentUserId = getCurrentUserId();
            log.info("Current User ID initialized: " + currentUserId);
        } catch (Exception e) {
        	log.error("Failed to fetch current user ID during startup", e);
            throw new RuntimeException("Failed to fetch current user ID during startup", e);
        }
    }
    
    private String buildUrl(String endpoint) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl).
        		pathSegment(endpoint).
        		toUriString();
    }
    
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }
    
    private ResponseEntity<String> makeGetRequest(String url) {
        return restTemplate.exchange(url, 
        		HttpMethod.GET, 
        		new HttpEntity<>(buildHeaders()), 
        		String.class);
    }
	
	private int getCurrentUserId() {
		String url = buildUrl("get_current_user");
		ResponseEntity<String> response = makeGetRequest(url);
		try {
			JsonNode userNode = objectMapper.readTree(response.getBody()).path("user");
			return userNode.path("id").asInt();
		} catch (Exception e) {
			log.error("Error parsing current user ID from response", e);
            throw new RuntimeException("Error parsing current user ID from response", e);
        }
	}
	
	public String getSplitwiseData(String endpoint) {
        String url = buildUrl(endpoint);
        ResponseEntity<String> response = makeGetRequest(url);
        log.info("Fetched data from Splitwise for endpoint: {}", endpoint);
        return response.getBody();
    }
	
	private GroupFriend parseGroup(JsonNode groupNode) {
        GroupFriend group = new GroupFriend();
        group.setId(groupNode.path("id").asInt());
        group.setName(groupNode.path("name").asText());
        group.setType("group");

        List<GroupFriend> members = new ArrayList<>();
        groupNode.path("members").forEach(memberNode -> members.add(parseFriend(memberNode)));
        group.setParticipants(members);

        return group;
    }
	
	private GroupFriend parseFriend(JsonNode friendNode) {
        GroupFriend friend = new GroupFriend();
        String firstName = friendNode.path("first_name").asText();
        String lastName = friendNode.path("last_name").asText("");

        friend.setId(friendNode.path("id").asInt());
        friend.setName(firstName + (lastName.isEmpty() ? "" : " " + lastName));
        friend.setType("friend");

        return friend;
    }
	
	public List<GroupFriend> parseGroupsAndFriends(String groupResponseBody, String friendResponseBody) {
		List<GroupFriend> groupAndFriendList = new ArrayList<>();
		
		try {
			JsonNode groupsNode = objectMapper.readTree(groupResponseBody).path("groups");
            groupsNode.forEach(groupNode -> groupAndFriendList.add(parseGroup(groupNode)));

            JsonNode friendsNode = objectMapper.readTree(friendResponseBody).path("friends");
            friendsNode.forEach(friendNode -> groupAndFriendList.add(parseFriend(friendNode)));
            log.info("Fetched groups and friends successfully");
		} catch (Exception e) {
			log.error("Error parsing groups and friends", e);
            throw new RuntimeException("Error parsing groups and friends", e);
        }
		
		return groupAndFriendList;
	}
	
	private int findGroupId(JsonNode participants) {
        for (JsonNode participant : participants) {
            if ("group".equals(participant.path("type").asText())) {
                return participant.path("id").asInt();
            }
        }
        return 0;
    }
	
	private void splitEquallyParticipants(JsonNode participantList, Map<String, Object> requestBody, double amount, int groupId) {
		double splitAmount = Math.ceil(amount / (participantList.size() + 1));
		int index = 0;
		double totalCost = 0;

		log.info("Splitting amount equally for participants: {}", String.valueOf(splitAmount));
		for (JsonNode userNode : participantList) {
		    int userId = userNode.path("id").asInt();
		    requestBody.put("users__" + index + "__user_id", userId);
		    requestBody.put("users__" + index + "__owed_share", String.valueOf(splitAmount));
		    requestBody.put("users__" + index + "__paid_share", "0");
		    index++;
		    totalCost+=splitAmount;
		}
	    requestBody.put("users__" + index + "__user_id", currentUserId);
	    requestBody.put("users__" + index + "__owed_share", String.valueOf(splitAmount));
	    requestBody.put("users__" + index + "__paid_share", String.valueOf(amount));
	    totalCost+=splitAmount;
	    if(Math.abs(totalCost - amount) <= 0.01) {
	    	requestBody.put("cost", String.valueOf(totalCost));
	    }
    }
	
	private void splitEqually(JsonNode checkedList, Map<String, Object> requestBody, double amount) {
		double splitAmount = Math.ceil(amount / checkedList.size());
		boolean containsCurrentUser = false;
		int index = 0;
		double totalCost = 0;

		log.info("Splitting amount equally: {}", String.valueOf(splitAmount));
		for (JsonNode userNode : checkedList) {
		    int userId = userNode.path("id").asInt();
		    boolean isCurrentUser = userId == currentUserId;

		    requestBody.put("users__" + index + "__user_id", userId);
		    requestBody.put("users__" + index + "__owed_share", String.valueOf(splitAmount));
		    requestBody.put("users__" + index + "__paid_share", isCurrentUser ? String.valueOf(amount) : "0");

		    containsCurrentUser |= isCurrentUser; // Mark true if currentUserId is found
		    index++;
		    totalCost+=splitAmount;
		}

		// Add the current user if not in the list
		if (!containsCurrentUser) {
		    requestBody.put("users__" + index + "__user_id", currentUserId);
		    requestBody.put("users__" + index + "__owed_share", "0.0");
		    requestBody.put("users__" + index + "__paid_share", String.valueOf(amount));
		}
		 if(Math.abs(totalCost - amount) <= 0.01) {
	    	requestBody.put("cost", String.valueOf(totalCost));
		 }
    }

	private void splitByShares(JsonNode shares, Map<String, Object> requestBody, double amount) {
		int index = 0;
		double totalCost = 0;
		log.info("Splitting by shares with amount: {}", amount);
		for (JsonNode userNode: shares) {
            requestBody.put("users__" + index + "__user_id", userNode.path("id").asInt());
            requestBody.put("users__" + index + "__owed_share", userNode.path("amount"));
            requestBody.put("users__" + index + "__paid_share", userNode.path("id").asInt() == currentUserId ? String.valueOf(amount) : "0");
            index++;
            totalCost+=userNode.path("amount").asDouble();
        }
		requestBody.put("cost", String.valueOf(totalCost));
    }
	
	private void handleCustomSplit(JsonNode expenseNode, Map<String, Object> requestBody, double amount) {
        String splitType = expenseNode.path("activeSplitTab").asText("");

        if ("SplitEqually".equals(splitType)) {
            splitEqually(expenseNode.path("checkedList"), requestBody, amount);
        } else if ("SplitShares".equals(splitType)) {
            splitByShares(expenseNode.path("shareAmount"), requestBody, amount);
        } else {
        	log.error("Unsupported split type: {}", splitType);
            throw new IllegalArgumentException("Unsupported split type: " + splitType);
        }
    }
	
	private void populateExpenseRequest(JsonNode expenseNode, Map<String, Object> requestBody) {
        int groupId = findGroupId(expenseNode.path("participants"));
        double amount = expenseNode.path("amount").asDouble();
        boolean splitEqually = !expenseNode.path("isEquallyClicked").asBoolean();

        requestBody.put("group_id", groupId);
        requestBody.put("description", expenseNode.path("description").asText(""));
        requestBody.put("cost", String.valueOf(amount));
        requestBody.put("currency_code", CURRENCY_CODE);

        if (splitEqually) {
        	if ( groupId == 0)
        		splitEquallyParticipants(expenseNode.path("participants"), requestBody, amount, groupId);
        	else
        		requestBody.put("split_equally", true);
            
        } else {
            handleCustomSplit(expenseNode, requestBody, amount);
        }
    }
	
	public String createExpense(String expenseBody) {
		String url = buildUrl("create_expense");
		Map<String, Object> requestBody = new HashMap<>();
		try 
		{
			log.info("Received expense data: {}", expenseBody);
            JsonNode expenseNode = objectMapper.readTree(expenseBody);
			populateExpenseRequest(expenseNode, requestBody);

			String jsonString = objectMapper.writeValueAsString(requestBody);
			log.info("Request body populated: {}", jsonString);
			
		} catch (Exception e) {
			log.error("Error processing expense data", e);
			throw new RuntimeException("Error processing expense data", e);
        }
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, buildHeaders());
		log.info("Request : {}", entity);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("Expense created successfully, response: {}", response.getBody());
        return response.getBody();
	}

}