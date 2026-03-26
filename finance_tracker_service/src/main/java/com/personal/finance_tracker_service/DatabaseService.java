package com.personal.finance_tracker_service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.personal.finance_tracker_service.repository.CategoryRepository;
import com.personal.finance_tracker_service.repository.ExpenseRepository;
import com.personal.finance_tracker_service.repository.IncomeRepository;
import com.personal.finance_tracker_service.repository.InvestmentRepository;
import com.personal.finance_tracker_service.database.Category;
import com.personal.finance_tracker_service.database.Expense;
import com.personal.finance_tracker_service.database.Income;
import com.personal.finance_tracker_service.database.Investment;
import com.personal.finance_tracker_service.database.CategoryExpenseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {
	private final ExpenseRepository expenseRepository;
	private final CategoryRepository categoryRepository;
	private final IncomeRepository incomeRepository;
	private final InvestmentRepository investmentRepository;
	
	public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
	
	public List<Category> getCategories(String type) {
        return categoryRepository.findByType(type);
    }
	
	public List<Investment> getAllInvestments() {
        return investmentRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }
	
	public List<Income> getAllIncomes() {
        return incomeRepository.findAll(Sort.by(Sort.Direction.DESC, "income_date"));
    }
	
	public List<Income> getUnreviewedIncomes() {
		return incomeRepository.findUnreviewed(Sort.by(Sort.Direction.DESC, "income_date"));
    }
	
	public List<Expense> getAllExpenses() {
		return expenseRepository.findAll(Sort.by(Sort.Direction.DESC, "expense_date"));
    }
	
	public List<Expense> getUnreviewedExpenses() {
		return expenseRepository.findUnreviewed(Sort.by(Sort.Direction.DESC, "expense_date"));
    }
	
	public void createExpense(Date expenseDate, String description, double amount, String source) {
		Expense expense = new Expense();
//		expense.setExpense_id(String.valueOf(expenseDate)+ String.valueOf(amount) + source); 
        expense.setExpense_date(expenseDate); // Adjust parsing as needed
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setReviewed(false);
        expense.setSource(source);
        expense.setCreated_date(Date.valueOf(LocalDate.now()));

        // Save to database
        expenseRepository.save(expense);
	}
	
	public void createIncome(Date incomeDate, String description, double amount, String source) {
		Income income = new Income();
//		income.setIncome_id(String.valueOf(incomeDate)+ String.valueOf(amount) + source); 
		income.setIncome_date(incomeDate); // Adjust parsing as needed
		income.setAmount(amount);
		income.setDescription(description);
		income.setReviewed(false);
		income.setSource(source);
		income.setCreated_date(Date.valueOf(LocalDate.now()));

        // Save to database
        incomeRepository.save(income);
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
	            case "is_deleted":
	            	expense.set_deleted((Boolean) value);
	            	break;
	            default:
	                throw new IllegalArgumentException("Invalid field: " + key);
	        }
	    });

	    expenseRepository.save(expense);
	    return ResponseEntity.ok("Expense updated successfully");
	}
	
	public ResponseEntity<String> updateIncome(String income_id, Map<String, Object> updates) {
		if (!incomeRepository.existsById(income_id)) {
	        return ResponseEntity.notFound().build();
	    }
		System.out.println(updates);
	    Income income = incomeRepository.findById(income_id).orElseThrow();

	    updates.forEach((key, value) -> {
	        switch (key) {
	            case "date":
	            	income.setIncome_date(Date.valueOf((String) value)); // Convert String to Date
	                break;
	            case "amount":
	            	income.setAmount(((Number) value).doubleValue());
	                break;
	            case "category":
	            	int categoryId = ((Number) value).intValue();
	                Category category = categoryRepository.findById(categoryId)
	                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
	                income.setCategory_id(category);
	                break;
	            case "description":
	            	income.setDescription((String) value);
	                break;
	            case "reviewed":
	            	income.setReviewed((Boolean) value);
	            	break;
	            case "is_deleted":
	            	income.set_deleted((Boolean) value);
	            	break;
	            default:
	                throw new IllegalArgumentException("Invalid field: " + key);
	        }
	    });
	    incomeRepository.save(income);
	    return ResponseEntity.ok("Income updated successfully");
	}
	
//	public CategoryExpenseDTO getCategoryExpenses() {
//	    List<Object[]> results = expenseRepository.getCategoryExpenses();
//	    System.out.println("getCategoryExpenses: " + results);
//	    Map<String, CategoryExpenseDTO> categoryMap = new HashMap<>();
//	    CategoryExpenseDTO root = new CategoryExpenseDTO("Expenses", 0);
//
//	    for (Object[] row : results) {
//	        String parentName = (String) row[0];
//	        String childName = (String) row[1];
//	        Double amount = (Double) row[2];
//
//	        CategoryExpenseDTO child = new CategoryExpenseDTO(childName, amount);
//	        categoryMap.put(childName, child);
//
//	        if (parentName == null) {
//	            root.getChildren().add(child);
//	        } else {
//	            categoryMap.computeIfAbsent(parentName, k -> new CategoryExpenseDTO(parentName, 0))
//	                       .getChildren().add(child);
//	        }
//	    }
//
//	    return root;
//	}
//	
	public CategoryExpenseDTO getCategoryExpenses(String filter) {
	    Integer year = null;
	    Integer month = null;

	    if (!filter.equals("All Transactions")) {
	        String[] parts = filter.split("-");
	        year = Integer.parseInt(parts[0]);
	        if (parts.length == 2) {
	            month = Integer.parseInt(parts[1]); // Extract month if available
	        }
	    }

	    List<Object[]> queryResult = expenseRepository.getCategoryExpensesByFilter(year, month);
	    return buildCategoryTree(queryResult);
	}
	
	public CategoryExpenseDTO buildCategoryTree(List<Object[]> queryResult) {
	    CategoryExpenseDTO root = new CategoryExpenseDTO("Expenses", 0);
	    Map<String, CategoryExpenseDTO> categoryMap = new HashMap<>();

	    for (Object[] row : queryResult) {
	        String parentCategory = (String) row[0];  // Parent category name
	        String category = (String) row[1];        // Sub-category name
	        double amount = (double) row[2];          // Expense amount

	        // Find or create parent category
	        CategoryExpenseDTO parentNode = categoryMap.computeIfAbsent(
	            parentCategory != null ? parentCategory : "Expenses",
	            k -> new CategoryExpenseDTO(k, 0)
	        );

	        // Find or create sub-category
	        CategoryExpenseDTO subCategoryNode = categoryMap.computeIfAbsent(
	            category, k -> new CategoryExpenseDTO(k, amount)
	        );

	        // Add the sub-category under the parent
	        if (!parentNode.getChildren().contains(subCategoryNode)) {
	            parentNode.getChildren().add(subCategoryNode);
	        }

	        // If parent is "Expenses", attach to root
	        if (parentCategory == null) {
	            root.getChildren().add(subCategoryNode);
	        }

	        // Update the value in the map (ensures parent sums up)
	        parentNode.setValue(parentNode.getValue() + amount);
	    }

	    return root;
	}




}
