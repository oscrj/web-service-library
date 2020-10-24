package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.services.BookService;
import com.example.webservicelibrary.services.LibraryUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/library-api/v1/library")
@Slf4j
public class LibraryController {
    @Autowired
    private LibraryUserService userService;
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) boolean sortOnPublishedDate) {

        return ResponseEntity.ok(bookService.findAllBooks(title, sortOnPublishedDate));
    }

}
