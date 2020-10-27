package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.entities.Movie;
import com.example.webservicelibrary.services.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library-api/v1/library")
@Slf4j
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<Object>> findAll() {
        return ResponseEntity.ok(libraryService.findAll());
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> findAllAvailableBooks(@RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String author,
                                                            @RequestParam(required = false) String genre,
                                                            @RequestParam(required = false) String publishedYear,
                                                            @RequestParam(required = false) boolean sortOnGenre){
        return ResponseEntity.ok(libraryService.findAllAvailableBooks(title, author, genre, publishedYear, sortOnGenre));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/books/borrow/{id}")
    public ResponseEntity<Book> borrowBook(@PathVariable String id) {
        return ResponseEntity.ok(libraryService.borrowBook(id));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/books/return/{id}")
    public ResponseEntity<Book> returnBook(@PathVariable String id){
        return ResponseEntity.ok(libraryService.returnBook(id));
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> findAllAvailableMovies(@RequestParam(required = false) String title,
                                                              @RequestParam(required = false) String director,
                                                              @RequestParam(required = false) String genre,
                                                              @RequestParam(required = false) String publishedYear,
                                                              @RequestParam(required = false) boolean sortOnGenre) {
        return ResponseEntity.ok(libraryService.findAllAvailableMovies(title, director, genre, publishedYear, sortOnGenre));
    }
}
