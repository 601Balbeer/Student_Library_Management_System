package com.example.student_library_management_system.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title",nullable = false)
    private  String title;

    @Column(name = "publisher_name",nullable = false)
    private String publisherName;

    @Column(name = "published_date",nullable = false)
    private String publishedDate;

    @Column(name = "pages",nullable = false)
    private int pages;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies = 1;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies = 1;

    @Column(name = "category",nullable = false)
    private String category;

    @Column(name = "rack_no",nullable = false)
    private String rackNo;

    @JsonManagedReference("book-transactions")
    @OneToMany(mappedBy = "book")
    private List<Transaction> transactionList;

    /** Initializes copy counts for book rows created before inventory tracking was added. */
    @PostLoad
    private void initializeLegacyCopyCounts() {
        if (totalCopies <= 0) {
            totalCopies = 1;
            availableCopies = 1;
        }
    }
}
