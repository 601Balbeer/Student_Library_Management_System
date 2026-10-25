package com.example.student_library_management_system.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="mobile", nullable = false,unique = true)
    private String mobile;

    @Column(name="dept", nullable = false)
    private String dept;

    @Column(name="sem", nullable = false)
    private String sem;

    @Column(name="gender", nullable = false)
    private String gender;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="dob", nullable = false)
    private String dob;

    @JsonManagedReference("student-card")
    @OneToOne(mappedBy = "student",cascade = CascadeType.ALL) //one student will have one card
    private Card card;


}
