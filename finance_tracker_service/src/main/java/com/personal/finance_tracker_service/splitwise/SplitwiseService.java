package com.personal.finance_tracker_service.splitwise;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    
    private String buildUrl(String endpoint, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment(endpoint);

        // Add query parameters if they exist
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        return builder.toUriString();
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
		String url = buildUrl("get_current_user", null);
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
        String url = buildUrl(endpoint,null);
        if (endpoint.equals("get_expenses")) {
        	url = buildUrl(endpoint,Map.of("limit", "100"));
        }
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
	    int participantCount = participantList.size() + 1; // Including the current user
	    double splitAmount = amount / participantCount;
	    double totalCost = 0;
	    int index = 0;

	    log.info("Splitting amount equally for participants: {}", splitAmount);

	    // Store the participant split data
	    for (JsonNode userNode : participantList) {
	        int userId = userNode.path("id").asInt();
	        double roundedSplitAmount = Math.round(splitAmount * 100.0) / 100.0; // Round to 2 decimal places
	        requestBody.put("users__" + index + "__user_id", userId);
	        requestBody.put("users__" + index + "__owed_share", String.valueOf(roundedSplitAmount));
	        requestBody.put("users__" + index + "__paid_share", "0");
	        totalCost += roundedSplitAmount;
	        index++;
	    }

	    // Add the current user to the split
	    double roundedSplitAmount = Math.round(splitAmount * 100.0) / 100.0; // Round to 2 decimal places
	    requestBody.put("users__" + index + "__user_id", currentUserId);
	    requestBody.put("users__" + index + "__owed_share", String.valueOf(roundedSplitAmount));
	    requestBody.put("users__" + index + "__paid_share", String.valueOf(amount));
	    totalCost += roundedSplitAmount;

	    // Adjust the total cost to match the exact amount
	    double adjustment = amount - totalCost;
	    if (Math.abs(adjustment) > 0.005) { // Significant difference
	        // Apply adjustment to the first user's owed share
	        String key = "users__0__owed_share";
	        double adjustedShare = Double.parseDouble(requestBody.get(key).toString()) + adjustment;
	        requestBody.put(key, String.valueOf(Math.round(adjustedShare * 100.0) / 100.0)); // Ensure adjustment is rounded
	        totalCost += adjustment;
	    }

	    requestBody.put("cost", String.valueOf(Math.round(totalCost * 100.0) / 100.0)); // Ensure total cost is rounded
	    log.info("Final cost adjusted to: {}", totalCost);
	}

	
	private void splitEqually(JsonNode checkedList, Map<String, Object> requestBody, double amount) {
	    int totalCents = (int) Math.round(amount * 100); // Convert to cents
	    int splitCents = totalCents / checkedList.size(); // Equal split in cents
	    int remainder = totalCents % checkedList.size(); // Remainder for adjustment
	    
	    boolean containsCurrentUser = false;
	    int index = 0;
	    int totalDistributed = 0;
	
	    log.info("Splitting amount equally: {}", splitCents / 100.0);

		try {
			String jsonString = objectMapper.writeValueAsString(requestBody);
			log.info("Before splitting Request body populated: {}", jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	    // Loop through users and calculate their owed shares
	    for (JsonNode userNode : checkedList) {
	        int userId = userNode.path("id").asInt();
	        boolean isCurrentUser = userId == currentUserId;
	
	        int userOwedCents = splitCents + (remainder > 0 ? 1 : 0); // Distribute remainder
	        remainder--;
	
	        requestBody.put("users__" + index + "__user_id", userId);
	        requestBody.put("users__" + index + "__owed_share", String.format("%.2f", userOwedCents / 100.0));
	        requestBody.put("users__" + index + "__paid_share", isCurrentUser ? String.format("%.2f", amount) : "0");
	
	        containsCurrentUser |= isCurrentUser;
	        totalDistributed += userOwedCents;
	        index++;
	    }
	    
	    // Add the current user if not in the list
	    if (!containsCurrentUser) {
	        requestBody.put("users__" + index + "__user_id", currentUserId);
	        requestBody.put("users__" + index + "__owed_share", String.format("%.2f", 0.00));
	        requestBody.put("users__" + index + "__paid_share", String.format("%.2f", amount));
	    }
	    
	    // Verify and adjust the total distributed cost
	    if (totalDistributed != totalCents) {
	        int adjustment = totalCents - totalDistributed;
	        String firstUserKey = "users__0__owed_share";
	        double firstUserOwed = Double.parseDouble(requestBody.get(firstUserKey).toString());
	        requestBody.put(firstUserKey, String.format("%.2f", (firstUserOwed * 100 + adjustment) / 100.0));
	    }

	    // Update the total cost in the request body
	    requestBody.put("cost", String.format("%.2f", amount));
	    log.info("Final cost adjusted to: {}", amount);
	}

	private void splitByShares(JsonNode shares, Map<String, Object> requestBody, double amount) {
	    int index = 0;
	    double totalCost = 0;
	
	    log.info("Splitting by shares with amount: {}", amount);
	
	    // Loop through shares and calculate the total cost
	    for (JsonNode userNode : shares) {
	        int userId = userNode.path("id").asInt();
	        double shareAmount = userNode.path("amount").asDouble();
	        double roundedShareAmount = Math.round(shareAmount * 100.0) / 100.0; // Round to 2 decimal places
	
	        requestBody.put("users__" + index + "__user_id", userId);
	        requestBody.put("users__" + index + "__owed_share", String.valueOf(roundedShareAmount));
	        requestBody.put("users__" + index + "__paid_share", userId == currentUserId ? String.valueOf(amount) : "0");
	
	        totalCost += roundedShareAmount;
	        index++;
	    }
	
	    // Calculate the adjustment needed to make totalCost match the specified amount
	    double adjustment = amount - totalCost;
	    if (Math.abs(adjustment) > 0.005) { // Apply adjustment if significant
	        String firstUserOwedShareKey = "users__0__owed_share";
	        double firstUserOwedShare = Double.parseDouble(requestBody.get(firstUserOwedShareKey).toString());
	        requestBody.put(firstUserOwedShareKey, String.valueOf(Math.round((firstUserOwedShare + adjustment) * 100.0) / 100.0));
	        totalCost += adjustment;
	    }
	
	    // Update the total cost in the request body
	    requestBody.put("cost", String.valueOf(Math.round(totalCost * 100.0) / 100.0));
	    log.info("Final cost adjusted to: {}", totalCost);
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
		String url = buildUrl("create_expense", null);
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
		log.info("Request: {}", entity);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	        log.info("Expense created successfully, response: {}", response.getBody());
	        // Parse the response body to check for errors
	        try {
	            JsonNode responseJson = objectMapper.readTree(response.getBody());
	            if (responseJson.has("errors") && !responseJson.get("errors").isEmpty()) {
	                String errorMessage = responseJson.get("errors").toString();
	                log.error("Expense creation failed with errors: {}", errorMessage);
	                throw new RuntimeException("Failed to create expense. Errors: " + errorMessage);
	            }
	        } catch (Exception e) {
	            log.error("Error parsing response body", e);
	            throw new RuntimeException("Error parsing response body", e);
	        }
	        return response.getBody();
	    } else {
	        log.error("Expense not created successfully, status code: {}, response: {}", 
	                  response.getStatusCodeValue(), response.getBody());
	        throw new RuntimeException("Failed to create expense. Status Code: " 
	                + response.getStatusCodeValue() 
	                + ", Response: " 
	                + response.getBody());
	    }
	}
	
	public List<Expense> parseAllExpenses(String expenseResponseBody, String friendResponseBody) {
    List<Expense> expensesList = new ArrayList<>();

    try {
        JsonNode expensesNode = objectMapper.readTree(expenseResponseBody).path("expenses");
        JsonNode friendsNode = objectMapper.readTree(friendResponseBody).path("friends");

        for (JsonNode expenseNode : expensesNode) {
            if ("Settle all balances".equals(expenseNode.path("description").asText())) {
                continue; // Skip settlement expenses
            }

            JsonNode usersNode = expenseNode.path("users");
            int payerId = 0;
            String payerName = "";
            double owedShare = 0;
            boolean isOwed = false;

            // Identify payer and check if current user owes money
            for (JsonNode userNode : usersNode) {
                int userId = userNode.path("user_id").asInt();
                double paidShare = userNode.path("paid_share").asDouble();
                owedShare = userNode.path("owed_share").asDouble();

                if (userId == currentUserId && owedShare > 0) {
                    isOwed = true;
                } else if (paidShare > 0) {
                    payerId = userId;
                }
            }

            if (isOwed && payerId != 0) {
                // Find payer name in friends list
                for (JsonNode friendNode : friendsNode) {
                    if (friendNode.path("id").asInt() == payerId) {
                        String firstName = friendNode.path("first_name").asText();
                        String lastName = friendNode.path("last_name").asText("");
                        payerName = firstName + (lastName.isEmpty() ? "" : " " + lastName);
                        break; // Exit loop once payer is found
                    }
                }
            }

            if (isOwed && !payerName.isEmpty()) {
            	Expense expense = new Expense();
            	expense.setId(expenseNode.path("id").asInt());
            	expense.setDate(expenseNode.path("date").asText());
            	expense.setAmount(expenseNode.path("cost").asDouble());
            	expense.setDescription(expenseNode.path("description").asText());
            	expense.setOwed_share(owedShare);
            	expense.setPayer_name(payerName);
                expensesList.add(expense);
            }
        }

        log.info("Fetched all owed expenses successfully");
    } catch (Exception e) {
        log.error("Error fetching expenses", e);
        throw new RuntimeException("Error fetching expenses", e);
    }

    return expensesList;
}

}