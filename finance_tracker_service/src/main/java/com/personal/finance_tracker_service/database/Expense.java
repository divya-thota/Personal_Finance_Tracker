package com.personal.finance_tracker_service.database;


import lombok.Data;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "Expenses")
@Data
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private String expense_id;

    @Column(name = "expense_date")
    private Date expense_date;

    @Column(name = "amount")
    private double amount;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "source")
    private String source;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category_id;
    
    @Column(name = "reviewed", columnDefinition="tinyint(1) default 0")
    private boolean reviewed;

    @Column(name = "is_deleted", columnDefinition="tinyint(1) default 0")
    private boolean is_deleted;
    
    @Column(name = "created_date")
    private Date created_date;
    
}