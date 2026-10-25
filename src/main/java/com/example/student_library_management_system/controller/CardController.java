package com.example.student_library_management_system.controller;

import com.example.student_library_management_system.model.Card;
import com.example.student_library_management_system.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card/apis")
public class CardController {
    private final CardService service;

    /** Creates the controller with its card service. */
    public CardController(CardService service) {
        this.service = service;
    }

    /** Returns all library cards. */
    @GetMapping("/findAll") public List<Card> findAll() {
        return service.findAll();
    }
    /** Returns a card by its identifier. */
    @GetMapping("/findById/{id}") public Card findById(@PathVariable int id) {
        return service.findById(id);
    }
    /** Returns the card assigned to a student. */
    @GetMapping("/findByStudent/{studentId}") public Card findByStudent(@PathVariable int studentId) {
        return service.findByStudentId(studentId);
    }
    /** Renews a card for the requested number of years. */
    @PatchMapping("/renew/{id}") public Card renew(@PathVariable int id, @RequestParam(defaultValue = "3") int years) {
        return service.renew(id, years);
    }
}
