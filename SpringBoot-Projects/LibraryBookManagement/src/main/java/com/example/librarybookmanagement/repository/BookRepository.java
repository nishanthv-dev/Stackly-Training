
package com.example.librarybookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.librarybookmanagement.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
