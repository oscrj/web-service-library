package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Book;
import com.example.webservicelibrary.entities.Movie;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
    public List<Object> findAll() {
        log.info("Request to find all Items.");

        var books = bookRepository.findAll() ;
        var movies = movieRepository.findAll();
        List<Object> allItems = new ArrayList<>();
        allItems.addAll(books);
        allItems.addAll(movies);

        return allItems;
    }

    @Cacheable(value = "libraryCache")
    public List<Book> findAllAvailableBooks(String title, String author, String genre, boolean sortOnGenre) {
        log.info("Request to find all available books.");
        var books = bookRepository.findAll();
        books = books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
        if (title != null) {
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (author != null) {
            books = books.stream()
                    .filter(book -> book.getAuthor().toLowerCase().startsWith(author.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (genre != null) {
            books = books.stream()
                    .filter(book -> book.getGenre().toLowerCase().startsWith(genre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (sortOnGenre) {
            books.sort(Comparator.comparing(Book::getGenre));
        }
        return books;
    }

    /**
     * Adds book to the current logged in users list of borrowed books.
     * @param id is used to receive the book that will be borrowed and put into the list of borrowed book of the current
     *           user.
     * @return the book that was borrowed.
     */
    @CachePut(value = "libraryCache", key = "#id")
    public Book borrowBook(String id){
        var currentUser = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var book = bookService.findBookById(id);

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
            log.error("User " + currentUser.getUsername() + " try to rent a book that is already borrowed.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The book is already rented..");
        }else {
            log.info(currentUser.getUsername() + " borrowed the book " + book.getTitle());
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
        var currentUser = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var book = bookService.findBookById(id);

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
            log.error("User " + currentUser.getUsername() + " try to return a book user hasn't borrowed");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You have not borrowed this book, and can not return it");
        }else {
            log.info(currentUser.getUsername() + " returned the book " + book.getTitle());
            var newList = currentUser.getBorrowedBooks();
            newList.remove(book);
            currentUser.setBorrowedBooks(newList);
            book.setAvailable(!book.isAvailable());
            userRepository.save(currentUser);
            bookRepository.save(book);
            return book;
        }
    }

    @Cacheable(value = "libraryCache")
    public List<Movie> findAllAvailableMovies(String title, String director, String genre, boolean sortOnGenre) {
        log.info("Request to find all available movies.");
        var movies = movieRepository.findAll();
        movies = movies.stream()
                .filter(Movie::isAvailable)
                .collect(Collectors.toList());

        if (title != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (director != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getDirector().toLowerCase().startsWith(director.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (genre != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getGenre().toLowerCase().startsWith(genre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (sortOnGenre) {
            movies.sort(Comparator.comparing(Movie::getGenre));
        }
        return movies;
    }
}
