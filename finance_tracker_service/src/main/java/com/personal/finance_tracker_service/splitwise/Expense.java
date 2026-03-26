package com.personal.finance_tracker_service.splitwise;

import lombok.Data;

@Data
public class Expense {
	private int id;
    private String payer_name;
    private String date;
    private String description;
    private double amount;
    private double owed_share;

}
