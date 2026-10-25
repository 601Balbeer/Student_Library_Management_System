package com.example.student_library_management_system.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="transaction_date",nullable = false)
    @CreationTimestamp
    private Date transactionDate;

    @Column(name="borrow_date")
    private String borrowDate;

    @Column(name="due_date",nullable = false)
    private String dueDate;

    @Column(name="transaction_type",nullable = false)
    private String transactionStatus;

    @JsonBackReference("card-transactions")
    @JoinColumn
    @ManyToOne
    private Card card;

    @JsonBackReference("book-transactions")
    @JoinColumn
    @ManyToOne
    private Book book;
}
