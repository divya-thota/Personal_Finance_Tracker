package com.personal.finance_tracker_service.database;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryExpenseDTO {
    private String name;
    private double value;
    private List<CategoryExpenseDTO> children = new ArrayList<>();

    public CategoryExpenseDTO(String name, double value) {
        this.name = name;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(CategoryExpenseDTO child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
