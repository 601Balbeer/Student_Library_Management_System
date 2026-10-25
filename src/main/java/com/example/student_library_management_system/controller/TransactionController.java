package com.example.student_library_management_system.controller;

import com.example.student_library_management_system.model.Transaction;
import com.example.student_library_management_system.requestdto.TransactionRequestDto;
import com.example.student_library_management_system.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/transaction/apis")
public class TransactionController {
    private final TransactionService service;

    /** Creates the controller with its transaction service. */
    public TransactionController(TransactionService service) { this.service = service; }

    /** Handles a request to borrow a book. */
    @PostMapping("/borrow") public ResponseEntity<?> borrow(@RequestBody TransactionRequestDto dto) {
        try {
            return ResponseEntity.ok(service.borrow(dto));
        } catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
        }
    }
    /** Handles a request to return a book. */
    @PostMapping("/return/{bookId}") public ResponseEntity<?> returnBook(@PathVariable int bookId, @RequestParam int cardId) {
        try {
            return ResponseEntity.ok(service.returnBook(bookId, cardId));
        } catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
        }
    }
    /** Returns all lending history. */
    @GetMapping("/findAll") public List<Transaction> findAll() { return service.findAll(); }
    /** Returns lending history for a card. */
    @GetMapping("/byCard/{cardId}") public List<Transaction> byCard(@PathVariable int cardId) { return service.findByCard(cardId); }
    /** Returns lending history for a book. */
    @GetMapping("/byBook/{bookId}") public List<Transaction> byBook(@PathVariable int bookId) { return service.findByBook(bookId); }

}
