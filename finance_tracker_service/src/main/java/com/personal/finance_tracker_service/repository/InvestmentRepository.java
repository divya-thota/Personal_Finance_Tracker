package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {

}
