package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.repositories.BookRepository;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import com.example.webservicelibrary.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryUserService userService;
    private final LibraryUserRepository userRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final MovieRepository movieRepository;

    @Cacheable(value = "libraryCache")
    public List<Book> findAll(String title) {
        log.info("Request to find all objects.");
        log.warn("Fresh Object data...");

        /*
        var books = bookRepository.findAll() ;
        var movies = movieRepository.findAll();
        List<Object> allObjects = new ArrayList<>();
        allObjects.addAll(books);
        allObjects.addAll(movies);
        */
        return bookRepository.findAll();
    }

    /**
     * Adds book to the current logged in users list of borrowed books.
     * @param id is used to receive the book that will be borrowed and put into the list of borrowed book of the current
     *           user.
     * @return the book that was borrowed.
     */
    @CachePut(value = "libraryCache", key = "#id")
    public Book borrowBook(String id){
        // find the logged in user...
        var currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        var book = bookService.findBookById(id);
        log.info(currentUser.getUsername() + " borrowed the book " + book.getTitle());

        if(!userRepository.existsById(currentUser.getId())){
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }
        if (!book.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The book is already rented..");
        }else {
            var newList = currentUser.getBorrowedBooks();
            newList.add(book);
            currentUser.setBorrowedBooks(newList);
            book.setAvailable(!book.isAvailable());

            userRepository.save(currentUser);
            bookRepository.save(book);
            return book;
        }
    }

    /**
     * Removes the book from the current logged in users list of borrowed books.
     * @param id is used to receive the book that will be returned and removed from the list of borrowed book of the
     *           current user.
     * @return the book that was returned.
     */
    @CachePut(value = "libraryCache", key = "#id")
    public Book returnBook(String id) {
        var currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        var book = bookService.findBookById(id);
        log.info(currentUser.getUsername() + " returned the book " + book.getTitle());

        if (!userRepository.existsById(currentUser.getId())) {
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find book by id %s.", id));
        }

        if (!currentUser.getBorrowedBooks().contains(book)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You have not borrowed this book, and can not return it");
        }

        var newList = currentUser.getBorrowedBooks();
        newList.remove(book);
        currentUser.setBorrowedBooks(newList);
        book.setAvailable(!book.isAvailable());

        userRepository.save(currentUser);
        bookRepository.save(book);
        return book;
    }
}
