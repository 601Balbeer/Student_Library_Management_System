package com.example.student_library_management_system.requestdto;

import lombok.Data;

@Data
public class StudentRequestDto {

    // request dto (Data Transfer Object)-It is used to take input into APIs.
    private String name;
    private String email;
    private String mobile;
    private String dept;
    private String sem;
    private String gender;
    private String address;
    private String dob;

}
