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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.finance_tracker_service.database.Category;
import com.personal.finance_tracker_service.database.Expense;
import com.personal.finance_tracker_service.database.Income;
import com.personal.finance_tracker_service.database.Investment;
import com.personal.finance_tracker_service.database.CategoryExpenseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/database")
@RequiredArgsConstructor
public class DatabaseController {
	
	private final DatabaseService databaseService;
	
	@GetMapping("/categories/{type}")
    public List<Category> getCategories(@PathVariable String type) {
        return databaseService.getCategories(type);
    }
	
	@GetMapping("/all_investments")
    public List<Investment> getAllInvestments() {
        return databaseService.getAllInvestments();
    }
	
	@GetMapping("/all_expenses")
    public List<Expense> getAllExpenses() {
        return databaseService.getAllExpenses();
    }
	
	@GetMapping("/unreviewed_expenses")
    public List<Expense> getUnreviewedExpenses() {
        return databaseService.getUnreviewedExpenses();
    }
	
	@GetMapping("/all_incomes")
    public List<Income> getAllIncomes() {
        return databaseService.getAllIncomes();
    }
	
	@GetMapping("/unreviewed_incomes")
    public List<Income> getUnreviewedIncomes() {
        return databaseService.getUnreviewedIncomes();
    }

	@PutMapping("/update_expense/{expense_id}")
	public ResponseEntity<String> updateExpense(@PathVariable String expense_id, @RequestBody Map<String, Object> updates) {
	    return databaseService.updateExpense(expense_id, updates);
	}
	
	@PutMapping("/update_income/{income_id}")
	public ResponseEntity<String> updateIncome(@PathVariable String income_id, @RequestBody Map<String, Object> updates) {
	    return databaseService.updateIncome(income_id, updates);
	}

	@GetMapping("/get_category_expense")
    public CategoryExpenseDTO getCategoryExpenses(@RequestParam String filter) {
        return databaseService.getCategoryExpenses(filter);
    }


}
