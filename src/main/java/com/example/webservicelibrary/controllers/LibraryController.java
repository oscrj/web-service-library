package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.services.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Object>> findAll(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) boolean sortOnPublishedDate) {
        return ResponseEntity.ok(libraryService.findAll(title, sortOnPublishedDate));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/library/borrow/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrow(@PathVariable String id, @RequestBody Book book){
        libraryService.borrowBook(id, book);
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN","ROLE_ADMIN"})
    @PutMapping("/library/return/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable String id, @RequestBody Book book){
        libraryService.returnBook(id, book);
    }


}
