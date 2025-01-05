package com.personal.finance_tracker_service.splitwise;

import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@Data
public class CurrentUserContext {
    private int currentUserId;
}