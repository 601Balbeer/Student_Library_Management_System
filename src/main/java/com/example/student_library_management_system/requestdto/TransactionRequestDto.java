package com.example.student_library_management_system.requestdto;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private String dueDate;
    private int cardId;
    private int bookId;
}
