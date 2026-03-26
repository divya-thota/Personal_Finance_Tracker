package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Income;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {
	
	@Query("SELECT e FROM Income e WHERE is_deleted = false")
    List<Income> findAll(Sort sort);
	
	
	@Query("SELECT e FROM Income e WHERE is_deleted = false AND reviewed = false")
    List<Income> findUnreviewed(Sort sort);
}
