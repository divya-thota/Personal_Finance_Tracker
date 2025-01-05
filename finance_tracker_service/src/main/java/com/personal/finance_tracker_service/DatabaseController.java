package com.personal.finance_tracker_service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.finance_tracker_service.database.Category;
import com.personal.finance_tracker_service.database.Expense;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/database")
@RequiredArgsConstructor
public class DatabaseController {
	
	private final DatabaseService databaseService;
	
	@GetMapping("/categories")
    public List<Category> getAllCategories() {
        return databaseService.getAllCategories();
    }
	
	@GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return databaseService.getAllExpenses();
    }
	
	@DeleteMapping("/delete_expense/{expense_id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String expense_id) {
        return databaseService.deleteExpense(expense_id);
    }
	
	@DeleteMapping("/delete_income/{income_id}")
    public ResponseEntity<String> deleteIncome(@PathVariable String income_id) {
        return databaseService.deleteIncome(income_id);
    }
	
	@PutMapping("/update_expense/{expense_id}")
	public ResponseEntity<String> updateExpense(@PathVariable String expense_id, @RequestBody Map<String, Object> updates) {
	    return databaseService.updateExpense(expense_id, updates);
	}



}
