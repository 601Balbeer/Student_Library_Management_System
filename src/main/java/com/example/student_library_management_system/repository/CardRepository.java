package com.example.student_library_management_system.repository;

import com.example.student_library_management_system.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
