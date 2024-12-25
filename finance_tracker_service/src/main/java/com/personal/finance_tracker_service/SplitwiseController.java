package com.example.splitwise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SplitwiseController {

    private final SplitwiseService splitwiseService;

    public SplitwiseController(SplitwiseService splitwiseService) {
        this.splitwiseService = splitwiseService;
    }

    @GetMapping("/api/groups")
    public String getGroups() {
        return splitwiseService.getGroups();
    }
}