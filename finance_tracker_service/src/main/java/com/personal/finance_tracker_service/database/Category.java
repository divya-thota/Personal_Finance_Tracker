package com.personal.finance_tracker_service.database;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parent_category_id;
    
    @Column(name="type")
    private String type;
}

