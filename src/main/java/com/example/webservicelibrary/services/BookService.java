package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    //@Cacheable(value = "libraryCache")
    public List<Book> findAllBooks(String title, boolean sortOnPublishedDate) {
        log.info("Request to find all books.");
        log.info("Fresh data...");

        var books = bookRepository.findAll();
        return books;
    }


}
