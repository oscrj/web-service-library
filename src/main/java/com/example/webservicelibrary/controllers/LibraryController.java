package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.entities.LibraryUser;
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
    public ResponseEntity<List<Book>> findAll(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(libraryService.findAll(title));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/borrow/{id}")
    public ResponseEntity<Book> borrowBook(@PathVariable String id) {
        return ResponseEntity.ok(libraryService.borrowBook(id));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/return/{id}")
    public ResponseEntity<Book> returnBook(@PathVariable String id){
        return ResponseEntity.ok(libraryService.returnBook(id));
    }
}
