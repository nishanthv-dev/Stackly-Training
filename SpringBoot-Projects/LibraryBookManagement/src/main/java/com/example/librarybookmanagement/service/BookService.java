
package com.example.librarybookmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarybookmanagement.model.Book;
import com.example.librarybookmanagement.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Add Book
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Get All Books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get Book By ID
    public Book getBookById(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    // Update Full Book
    public Book updateBook(int bookId, Book updatedBook) {

        Book existingBook = getBookById(bookId);

        if (existingBook != null) {

            existingBook.setBookName(updatedBook.getBookName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setAvailable(updatedBook.isAvailable());

            return bookRepository.save(existingBook);
        }

        return null;
    }

    // Update Availability
    public Book updateAvailability(int bookId, boolean available) {

        Book book = getBookById(bookId);

        if (book != null) {
            book.setAvailable(available);
            return bookRepository.save(book);
        }

        return null;
    }

    // Delete Book
    public boolean deleteBook(int bookId) {

        Book book = getBookById(bookId);

        if (book != null) {
            bookRepository.delete(book);
            return true;
        }

        return false;
    }
}
