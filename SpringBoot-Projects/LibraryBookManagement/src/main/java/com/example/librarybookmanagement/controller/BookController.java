package com.example.librarybookmanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.librarybookmanagement.model.Book;
import com.example.librarybookmanagement.service.BookService;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 1. Get All Books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 2. Get Book By ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {

        Book book = bookService.getBookById(id);

        if (book != null) {
            return ResponseEntity.ok(book);
        }

        return ResponseEntity.notFound().build();
    }

    // 3. Add Book
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {

        Book savedBook = bookService.addBook(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // 4. Update Complete Book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable int id,
            @Valid @RequestBody Book book) {

        Book updatedBook = bookService.updateBook(id, book);

        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }

        return ResponseEntity.notFound().build();
    }

    // 5. Update Book Availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<Book> updateAvailability(
            @PathVariable int id,
            @RequestParam boolean available) {

        Book book = bookService.updateAvailability(id, available);

        if (book != null) {
            return ResponseEntity.ok(book);
        }

        return ResponseEntity.notFound().build();
    }

    // 6. Delete Book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {

        boolean deleted = bookService.deleteBook(id);

        if (deleted) {
            return ResponseEntity.ok("Book deleted successfully");
        }

        return ResponseEntity.notFound().build();
    }
    

}