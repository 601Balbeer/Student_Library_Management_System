package com.example.student_library_management_system.requestdto;

import lombok.Data;

@Data
public class CardRequestDto {

    private String expiryDate;

    private int studentId;
}
