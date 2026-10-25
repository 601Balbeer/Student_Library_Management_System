package com.example.student_library_management_system.service;

import com.example.student_library_management_system.model.Book;
import com.example.student_library_management_system.repository.BookRepository;
import com.example.student_library_management_system.repository.TransactionRepository;
import com.example.student_library_management_system.requestdto.BookRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository books;
    private final TransactionRepository transactions;

    /** Creates the service with book and transaction repositories. */
    public BookService(BookRepository books, TransactionRepository transactions) {
        this.books = books;
        this.transactions = transactions;
    }

    /** Creates an available book using the supplied details. */
    public Book create(BookRequestDto dto) {
        Book book = new Book();
        apply(book, dto);
        int copies = dto.getTotalCopies() == null ? 1 : dto.getTotalCopies();
        if (copies < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "totalCopies must be greater than zero");
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies);
        return books.save(book);
    }

    /** Returns every book in the catalogue. */
    public List<Book> findAll() { return books.findAll(); }
    /** Finds a book by identifier or reports that it does not exist. */
    public Book findById(int id) { return books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book ID not found")); }
    /** Searches the catalogue by title, ignoring letter case. */
    public List<Book> findByTitle(String title) { return books.findByTitleContainingIgnoreCase(title); }
    /** Searches the catalogue by category, ignoring letter case. */
    public List<Book> findByCategory(String category) { return books.findByCategoryIgnoreCase(category); }
    /** Returns catalogue entries with at least one available copy. */
    public List<Book> findAvailable() { return books.findByAvailableCopiesGreaterThan(0); }

    /** Updates an existing book with validated request details. */
    public Book update(int id, BookRequestDto dto) {
        Book book = findById(id);
        apply(book, dto);
        if (dto.getTotalCopies() != null) {
            int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();
            if (dto.getTotalCopies() < borrowedCopies || dto.getTotalCopies() < 1)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "totalCopies cannot be less than the number of borrowed copies and must be greater than zero");
            book.setTotalCopies(dto.getTotalCopies());
            book.setAvailableCopies(dto.getTotalCopies() - borrowedCopies);
        }
        return books.save(book);
    }

    /** Deletes a book only when it is available and has no lending history. */
    @Transactional
    public void delete(int id) {
        Book book = findById(id);
        if (book.getAvailableCopies() != book.getTotalCopies()) throw new ResponseStatusException(HttpStatus.CONFLICT, "A book with borrowed copies cannot be deleted");
        if (transactions.existsByBookId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "A book with transaction history cannot be deleted");
        books.delete(book);
    }

    /** Validates request fields and copies them into a book entity. */
    private void apply(Book book, BookRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        if (dto.getPublisherName() == null || dto.getPublisherName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "publisherName is required");
        if (dto.getPages() < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pages must be greater than zero");
        book.setTitle(dto.getTitle().trim());
        book.setPublisherName(dto.getPublisherName().trim());
        book.setPublishedDate(dto.getPublishedDate());
        book.setPages(dto.getPages());
        book.setCategory(dto.getCategory());
        book.setRackNo(dto.getRackNo());
    }
}
