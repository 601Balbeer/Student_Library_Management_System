package com.example.student_library_management_system.service;

import com.example.student_library_management_system.model.Card;
import com.example.student_library_management_system.repository.CardRepository;
import com.example.student_library_management_system.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cards;
    private final StudentRepository students;

    /** Creates the service with card and student repositories. */
    public CardService(CardRepository cards, StudentRepository students) {
        this.cards = cards;
        this.students = students;
    }

    /** Returns all library cards. */
    public List<Card> findAll() {
        return cards.findAll();
    }
    /** Finds a card by identifier or reports that it does not exist. */
    public Card findById(int id) {
        return cards.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card ID not found"));
    }
    /** Finds the library card assigned to a student. */
    public Card findByStudentId(int studentId) {
        return students.findById(studentId).map(student -> {
            if (student.getCard() == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student has no library card");
            return student.getCard();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student " + studentId + " not found"));
    }
    /** Extends a card's expiry date. */
    public Card renew(int id, int years) {
        if (years < 1 || years > 5) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "renewal period must be between 1 and 5 years");
        Card card = findById(id);
        LocalDate currentExpiry;
        try {
            currentExpiry = LocalDate.parse(card.getExpiryDate());
        } catch (java.time.format.DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card expiry date must use YYYY-MM-DD format", exception);
        }
        LocalDate start = currentExpiry.isAfter(LocalDate.now()) ? currentExpiry : LocalDate.now();
        card.setExpiryDate(start.plusYears(years).toString());
        return cards.save(card);
    }
}
