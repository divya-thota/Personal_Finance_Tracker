package com.personal.finance_tracker_service.database;
import lombok.Data;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "Investments")
@Data
public class Investment {
	@Id
    private String investment_id;
	
	@Column(name = "date")
    private Date date;
	
	@Column(name = "investment_account")
	private String investment_account;
	
	@Column(name = "stock_name")
	private String stock_name;
    
    @Column(name="number_of_shares")
    private Double number_of_shares;

    @Column(name = "purchase_amount")
    private Double purchase_amount;
    
    @Column(name = "purchase_share_cost")
    private Double purchase_share_cost;
    
    @Column(name = "sell_amount")
    private Double sell_amount;
    
    @Column(name = "sell_share_cost")
    private Double sell_share_cost;

    @Column(name = "total_dividend")
    private Double total_dividend;
    
    @Column(name = "profits")
    private Double profits;
    
    @Column(name = "loss")
    private Double loss;
    
    @Column(name = "is_sold", columnDefinition="tinyint(1) default 0")
    private boolean is_sold;
    
}
