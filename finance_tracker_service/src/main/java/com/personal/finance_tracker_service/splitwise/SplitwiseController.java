package com.personal.finance_tracker_service.splitwise;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SplitwiseController {

    private final SplitwiseService splitwiseService;

    @GetMapping("/groups_friends")
    public List<GroupFriend> getGroupsAndFriends() {
        return splitwiseService.parseGroupsAndFriends(
                splitwiseService.getSplitwiseData("get_groups"),
                splitwiseService.getSplitwiseData("get_friends")
        );
    }

    @GetMapping("/groups")
    public String getGroups() {
        return splitwiseService.getSplitwiseData("get_groups");
    }

    @GetMapping("/friends")
    public String getFriends() {
        return splitwiseService.getSplitwiseData("get_friends");
    }

    @GetMapping("/expenses")
    public String getExpenses() {
        return splitwiseService.getSplitwiseData("get_expenses");
    }

    @PostMapping("/create_expense")
    public String createExpense(@RequestBody String expenseBody) {
        return splitwiseService.createExpense(expenseBody);
    }
}
