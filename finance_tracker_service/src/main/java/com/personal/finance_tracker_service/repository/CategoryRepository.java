package com.personal.finance_tracker_service.repository;

import com.personal.finance_tracker_service.database.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Custom query to find categories by name
    Category findByName(String name);
}
