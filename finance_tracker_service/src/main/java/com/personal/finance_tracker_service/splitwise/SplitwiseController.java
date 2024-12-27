package com.personal.finance_tracker_service.splitwise;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SplitwiseController {

	private final SplitwiseService splitwiseService;
	
	public SplitwiseController(SplitwiseService splitwiseService) {
		this.splitwiseService = splitwiseService;
	}
	
	@GetMapping("/api/groups_friends")
	public List<GroupFriend> getGroupsAndFriends() {
		return splitwiseService.parseGroupsAndFriends(
				splitwiseService.getSplitwise("get_groups"),
				splitwiseService.getSplitwise("get_friends"));
	}
	
	@GetMapping("/api/groups")
	public String getGroups() {
		return splitwiseService.getSplitwise("get_groups");
	}
	
	@GetMapping("/api/friends")
	public String getFriends() {
		return splitwiseService.getSplitwise("get_friends");
	}
	
	@GetMapping("/api/expenses")
	public String getExpenses() {
		return splitwiseService.getSplitwise("get_expenses");
	}
	
	@PostMapping("/api/create_expense")
	public String createExpense( @RequestBody String expenseBody) {
		return splitwiseService.createExpense(expenseBody);
	}

}