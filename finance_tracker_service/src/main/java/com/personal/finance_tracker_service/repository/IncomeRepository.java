package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {
	void deleteById(String id);
}
