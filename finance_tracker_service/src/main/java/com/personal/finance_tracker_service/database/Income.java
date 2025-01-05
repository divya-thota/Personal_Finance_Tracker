package com.personal.finance_tracker_service.database;


import lombok.Data;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "Incomes")
@Data
public class Income {
	@Id
    private String income_id;

    @Column(name = "income_date")
    private Date income_date;

    @Column(name = "amount")
    private double amount;
    
    @Column(name = "description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category_id;
    
    @Column(name = "reviewed", columnDefinition="tinyint(1) default 0")
    private boolean reviewed;
}