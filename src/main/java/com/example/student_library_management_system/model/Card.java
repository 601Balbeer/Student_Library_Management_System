package com.example.student_library_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="expiry_date",nullable = false)
    private  String expiryDate;

    @Column(name="created_date",nullable = false)
    @CreationTimestamp //when the new  card is created it will automatically add date and time
    private Date createdDate;

    @Column(name="updated_date",nullable = false)
    @UpdateTimestamp //when the card is updated it will automatically add date and  time
    private Date updatedDate;

    @JsonBackReference("student-card")
    @JoinColumn //It joins the primary key student id from student table as a foreign key in card table
    @OneToOne //one card will be assigned to one student
    private Student student;

    @JsonManagedReference("card-transactions")
    @OneToMany(mappedBy = "card")
    private List<Transaction> transactionList;
}
