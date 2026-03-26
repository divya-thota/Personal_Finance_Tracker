package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
	
	@Query("SELECT e FROM Expense e WHERE is_deleted = false")
    List<Expense> findAll(Sort sort);
	
	
	@Query("SELECT e FROM Expense e WHERE is_deleted = false AND reviewed = false")
    List<Expense> findUnreviewed(Sort sort);
	
	@Query("SELECT p.name, c.name, COALESCE(SUM(e.amount), 0) " +
		       "FROM Category c " +
		       "LEFT JOIN c.parent_category_id p " +
		       "LEFT JOIN Expense e ON c.category_id = e.category_id.category_id AND e.is_deleted = false " +
		       "GROUP BY p.name, c.name ORDER BY p.name, c.name")
	List<Object[]> getCategoryExpenses();
	
	@Query("SELECT COALESCE(p.name, 'Uncategorized'), COALESCE(c.name, 'Uncategorized'), COALESCE(SUM(e.amount), 0) " +
		       "FROM Expense e " +
		       "LEFT JOIN Category c ON e.category_id.category_id = c.category_id " +
		       "LEFT JOIN c.parent_category_id p " +
		       "WHERE e.is_deleted = false " +
		       "AND (:year IS NULL OR FUNCTION('YEAR', e.expense_date) = :year) " +
		       "AND (:month IS NULL OR FUNCTION('MONTH', e.expense_date) = :month OR :month IS NULL) " +
		       "GROUP BY p.name, c.name ORDER BY p.name, c.name")
		List<Object[]> getCategoryExpensesByFilter(@Param("year") Integer year, @Param("month") Integer month);

}
