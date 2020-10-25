package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryUserService userService;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final MovieService movieService;

    //@Cacheable(value = "libraryCache")
    public List<Object> findAll(String title, boolean sortOnPublishedYear, boolean sortOnRating) {
        log.info("Request to find all objects.");
        log.warn("Fresh data...");

        var books = bookService.findAllBooks(title,sortOnPublishedYear);
        var movies = movieService.findAllMovies(title, sortOnRating);
        List<Object> allObjects = new ArrayList<>();
        allObjects.addAll(books);
        allObjects.addAll(movies);

        return allObjects;
    }

    /**
     *
     * @param id
     * @param book
     */
    //@CachePut(value = "libraryCache", key = "#id")
    public void borrowBook(String id, Book book){

        // find the logged in user...
        var currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (currentUser == null) {
            throw new UsernameNotFoundException("You dont have authority to loan");
        }
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }

        var newList = currentUser.getBorrowedBooks();
        newList.add(book);
        currentUser.setBorrowedBooks(newList);
        book.setAvailable(false);

        userService.updateUser(currentUser.getId(), currentUser);
        bookService.updateBook(book.getId(), book);
    }

    /**
     *
     * @param id
     * @param book
     */
    //@CachePut(value = "libraryCache", key = "#id")
    public void returnBook(String id, Book book){

    }

}
