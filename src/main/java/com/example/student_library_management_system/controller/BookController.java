package com.example.student_library_management_system.controller;

import com.example.student_library_management_system.model.Book;
import com.example.student_library_management_system.requestdto.BookRequestDto;
import com.example.student_library_management_system.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book/apis")
public class BookController {
    private final BookService service;

    /** Creates the controller with its book service. */
    public BookController(BookService service) {
        this.service = service;
    }

    /** Creates a book from the request body. */
    @PostMapping("/save") public String save(@RequestBody BookRequestDto dto) {
        service.create(dto);
        return "Book saved successfully";
    }
    /** Returns all books. */
    @GetMapping("/findAll") public List<Book> findAll() {
        return service.findAll();
    }
    /** Returns a book by its identifier. */
    @GetMapping("/findById/{id}") public Book findById(@PathVariable int id) {
        return service.findById(id);
    }
    /** Searches books by title. */
    @GetMapping("/findByTitle") public List<Book> findByTitle(@RequestParam String title) {
        return service.findByTitle(title);
    }
    /** Searches books by category. */
    @GetMapping("/findByCategory") public List<Book> findByCategory(@RequestParam String category) {
        return service.findByCategory(category);
    }
    /** Returns books that are currently available. */
    @GetMapping("/available") public List<Book> available() {
        return service.findAvailable();
    }
    /** Updates a book using the supplied request body. */
    @PutMapping("/update/{id}") public Book update(@PathVariable int id, @RequestBody BookRequestDto dto) {
        return service.update(id, dto);
    }
    /** Deletes a book by its identifier. */
    @DeleteMapping("/delete/{id}") public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
