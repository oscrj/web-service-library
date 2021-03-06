package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable(value = "libraryCache")
    public List<Book> findAllBooks(String title, boolean sortOnPublishedYear) {
        log.info("Request to find all books.");
        log.info("Fresh Book data...");

        var books = bookRepository.findAll();
        if (title != null) {
            books = books.stream()
                    .filter(book -> book.getTitle().startsWith(title))
                    .collect(Collectors.toList());
        }
        if (sortOnPublishedYear) {
            books.sort(Comparator.comparing(Book::getPublishedYear));
        }
        return books;
    }

    @Cacheable(value = "libraryCache", key = "#id")
    public Book findBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Could not find the book by id %s.", id)));
    }

    @CachePut(value = "libraryCache", key = "#result.id")
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @CachePut(value = "libraryCache", key = "#id")
    public void updateBook(String id, Book book) {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        book.setId(id);
        bookRepository.save(book);
    }

    /**
     * Add a book cover.
     * @param file the file that will be the covor of the given book.
     * @param id the given books id, makes it easier to set the correct cover to the correct book.
     * @return the book that was updated.
     */
    @CachePut(value = "libraryCache", key = "#id")
    public Book addBookCover(MultipartFile file, String id) throws IOException {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        var book = findBookById(id);
        book.setBookCover(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        return bookRepository.save(book);
    }

    @CacheEvict(value = "libraryCache", key = "#id")
    public void deleteBook(String id) {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        bookRepository.deleteById(id);
    }
}
