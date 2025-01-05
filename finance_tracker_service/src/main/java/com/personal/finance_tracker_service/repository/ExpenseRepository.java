package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
	void deleteById(String id);
	
	@Query("SELECT e FROM Expense e")
    List<Expense> findAll(Sort sort);
}
