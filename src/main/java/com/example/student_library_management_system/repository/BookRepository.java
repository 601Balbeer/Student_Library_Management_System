package com.example.student_library_management_system.repository;

import com.example.student_library_management_system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    /** Finds books whose titles contain the search text, ignoring letter case. */
    List<Book> findByTitleContainingIgnoreCase(String title);
    /** Finds books in a category, ignoring letter case. */
    List<Book> findByCategoryIgnoreCase(String category);
    /** Finds catalogue entries with more than the specified available copy count. */
    List<Book> findByAvailableCopiesGreaterThan(int minimumAvailableCopies);
}
