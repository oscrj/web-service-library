package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.Movie;
import com.example.webservicelibrary.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library-api/v1/movies")
@Slf4j
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @GetMapping
    public ResponseEntity<List<Movie>> findAllMovies(@RequestParam(required = false) String title,
                                                     @RequestParam(required = false) boolean sortOnRating) {
        return ResponseEntity.ok(movieService.findAllMovies(title, sortOnRating));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @GetMapping("/{id}")
    public ResponseEntity<Movie> findMovieById(@PathVariable String id) {
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @PostMapping
    public ResponseEntity<Movie> saveMovie(@Validated @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        movieService.updateMovie(id, movie);
    }

    @Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
    }
}
