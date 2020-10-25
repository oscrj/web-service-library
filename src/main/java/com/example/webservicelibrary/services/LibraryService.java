package com.example.webservicelibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryUserService userService;
    private final BookService bookService;

    //@Cacheable(value = "libraryCache")
    public List<Object> findAll(String title, boolean sortOnPublishedDate) {
        log.info("Request to find all objects.");
        log.warn("Fresh data...");

        var books = bookService.findAllBooks(title,sortOnPublishedDate);

        List<Object> allObjects = new ArrayList<>();

        allObjects.addAll(books);

        return allObjects;
    }

    public void borrowBook(String title){

    }

    public void returnBook(String title){

    }



}
