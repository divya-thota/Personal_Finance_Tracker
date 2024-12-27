package com.personal.finance_tracker_service.splitwise;

import lombok.Data;
import java.util.List;

@Data
public class GroupFriend {
    private int id;
    private String name;
    private String type;
    private List<GroupFriend> participants;
    
}