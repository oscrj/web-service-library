package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (title != null) {
            books = books.stream()
                    .filter(book -> book.getTitle().startsWith(title))
                    .collect(Collectors.toList());
        }
        if (sortOnPublishedDate) {
            books.sort(Comparator.comparing(Book::getPublishedDate));
        }
        return books;
    }

    //@Cacheable(value = "libraryCache", key = "#id")
    public Book findBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Could not find the book by id %s.", id)));
    }

    //@CachePut(value = "libraryCache", key = "#result.id")
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    //@CachePut(value = "libraryCache", key = "#id")
    public void updateBook(String id, Book book) {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        book.setId(id);
        bookRepository.save(book);
    }

    //@CacheEvict(value = "libraryCache", key = "#id")
    public void deleteBook(String id) {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        bookRepository.deleteById(id);
    }
}
