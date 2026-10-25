package com.example.student_library_management_system.service;

import com.example.student_library_management_system.model.Book;
import com.example.student_library_management_system.model.Card;
import com.example.student_library_management_system.model.Transaction;
import com.example.student_library_management_system.repository.BookRepository;
import com.example.student_library_management_system.repository.CardRepository;
import com.example.student_library_management_system.repository.TransactionRepository;
import com.example.student_library_management_system.requestdto.TransactionRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactions;
    private final BookRepository books;
    private final CardRepository cards;

    /** Creates the service with the repositories used for lending operations. */
    public TransactionService(TransactionRepository transactions, BookRepository books, CardRepository cards) {
        this.transactions = transactions;
        this.books = books;
        this.cards = cards;
    }

    /** Borrows an available book for a non-expired card and records the due date. */
    public Transaction borrow(TransactionRequestDto dto) {
        Book book = books.findById(dto.getBookId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book ID not found"));
        Card card = validCard(dto.getCardId());
        if (book.getAvailableCopies() <= 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Books are not available");
        LocalDate dueDate;
        try {
            dueDate = dto.getDueDate() == null || dto.getDueDate().isBlank()
                    ? LocalDate.now().plusDays(14)
                    : LocalDate.parse(dto.getDueDate());
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dueDate must use YYYY-MM-DD format", exception);
        }
        if (dueDate.isBefore(LocalDate.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dueDate cannot be in the past");
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        books.save(book);
        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setBook(book);
        transaction.setBorrowDate(LocalDate.now().toString());
        transaction.setDueDate(dueDate.toString());
        transaction.setTransactionStatus("BORROWED");
        return transactions.save(transaction);
    }

    /** Returns a borrowed book when it belongs to the supplied non-expired card. */
    public Transaction returnBook(int bookId, int cardId) {
        Book book = books.findById(bookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book ID not found"));
        Card card = validCard(cardId);
        Transaction latestLoan = transactions.findFirstByBook_IdAndCard_IdAndTransactionStatusInOrderByTransactionDateDescIdDesc(bookId, cardId, List.of("BORROWED", "BORROW"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "This card has no active loan for this book"));
        if (book.getAvailableCopies() >= book.getTotalCopies())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All copies of this book are already marked as available");
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        books.save(book);
        latestLoan.setTransactionDate(new Date());
        latestLoan.setTransactionStatus("RETURNED");
        return transactions.save(latestLoan);
    }

    /** Retrieves transaction history ordered from newest to oldest. */
    public List<Transaction> findAll() {
        return transactions.findAllByOrderByTransactionDateDesc();
    }

    /** Retrieves transaction history for one library card, newest first. */
    public List<Transaction> findByCard(int cardId) {
        return transactions.findByCardIdOrderByTransactionDateDesc(cardId);
    }

    /** Retrieves transaction history for one book, newest first. */
    public List<Transaction> findByBook(int bookId) {
        return transactions.findByBookIdOrderByTransactionDateDesc(bookId);
    }

    /** Loads a card and checks that its expiry date has not passed. */
    private Card validCard(int id) {
        Card card = cards.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card ID not found"));
        try {
            if (LocalDate.parse(card.getExpiryDate()).isBefore(LocalDate.now()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Library card has expired");
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stored card expiry date is invalid", exception);
        }
        return card;
    }
}
