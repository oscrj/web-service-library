package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library-api/v1/books")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) boolean sortOnYear) {
        return ResponseEntity.ok(bookService.findAllBooks(title,sortOnYear));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findBookById(id));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @PostMapping
    public ResponseEntity<Book> saveBook(@Validated @RequestBody Book book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String id, @RequestBody Book book) {
        bookService.updateBook(id,book);
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }

}
