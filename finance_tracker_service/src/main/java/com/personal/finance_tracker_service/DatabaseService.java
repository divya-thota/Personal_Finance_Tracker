package com.personal.finance_tracker_service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.personal.finance_tracker_service.repository.CategoryRepository;
import com.personal.finance_tracker_service.repository.ExpenseRepository;
import com.personal.finance_tracker_service.repository.IncomeRepository;
import com.personal.finance_tracker_service.database.Category;
import com.personal.finance_tracker_service.database.Expense;
import com.personal.finance_tracker_service.database.Income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {
	private final ExpenseRepository expenseRepository;
	private final CategoryRepository categoryRepository;
	private final IncomeRepository incomeRepository;
	
	public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
	
	public List<Expense> getAllExpenses() {
		return expenseRepository.findAll(Sort.by(Sort.Direction.ASC, "expense_date"));
    }
	
	public void createExpense(Date expenseDate, String description, double amount) {
		Expense expense = new Expense();
		expense.setExpense_id(String.valueOf(expenseDate)+ String.valueOf(amount)); 
        expense.setExpense_date(expenseDate); // Adjust parsing as needed
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setReviewed(false);

        // Save to database
        expenseRepository.save(expense);
	}
	
	public void createIncome(Date incomeDate, String description, double amount) {
		Income income = new Income();
		income.setIncome_id(String.valueOf(incomeDate)+ String.valueOf(amount)); 
		income.setIncome_date(incomeDate); // Adjust parsing as needed
		income.setAmount(amount);
		income.setDescription(description);
		income.setReviewed(false);

        // Save to database
        incomeRepository.save(income);
	}
	
	
	public ResponseEntity<String> deleteIncome(String income_id) {
		if (!incomeRepository.existsById(income_id)) {
            return ResponseEntity.notFound().build();
        }
		incomeRepository.deleteById(income_id);
		return ResponseEntity.ok("Income deleted successfully");
	}
	
	public ResponseEntity<String> deleteExpense(String expense_id) {
		if (!expenseRepository.existsById(expense_id)) {
            return ResponseEntity.notFound().build();
        }
        expenseRepository.deleteById(expense_id);
        return ResponseEntity.ok("Expense deleted successfully");
	}
	
	public ResponseEntity<String> updateExpense(String expense_id, Map<String, Object> updates) {
		if (!expenseRepository.existsById(expense_id)) {
	        return ResponseEntity.notFound().build();
	    }
		System.out.println(updates);
	    Expense expense = expenseRepository.findById(expense_id).orElseThrow();

	    updates.forEach((key, value) -> {
	        switch (key) {
	            case "date":
	                expense.setExpense_date(Date.valueOf((String) value)); // Convert String to Date
	                break;
	            case "amount":
	                expense.setAmount(((Number) value).doubleValue());
	                break;
	            case "category":
	            	int categoryId = ((Number) value).intValue();
	                Category category = categoryRepository.findById(categoryId)
	                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
	                expense.setCategory_id(category);
	                break;
	            case "description":
	                expense.setDescription((String) value);
	                break;
	            case "reviewed":
	            	expense.setReviewed((Boolean) value);
	            	break;
	            default:
	                throw new IllegalArgumentException("Invalid field: " + key);
	        }
	    });

	    expenseRepository.save(expense);
	    return ResponseEntity.ok("Expense updated successfully");
	}

}
