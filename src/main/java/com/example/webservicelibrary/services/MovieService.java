package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.Movie;
import com.example.webservicelibrary.repositories.MovieRepository;
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
public class MovieService {

    private final MovieRepository movieRepository;

    @Cacheable(value = "libraryCache")
    public List<Movie> findAllMovies(String title, boolean sortOnRating) {
        log.info("Request to find all movies.");
        log.info("Fresh Movie data...");
        var movies = movieRepository.findAll();
        if (title != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getTitle().startsWith(title))
                    .collect(Collectors.toList());
        }
        if (sortOnRating) {
            movies.sort(Comparator.comparing(Movie::getRating));
        }
        return movies;
    }

    @Cacheable(value = "libraryCache", key = "#id")
    public Movie findMovieById(String id) {
      return movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              String.format("Could not find the book by id %s.", id)));
    }

    @CachePut(value = "libraryCachhe", key = "#result.id")
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @CachePut(value = "libraryCache", key = "#id")
    public void updateMovie(String id, Movie movie) {
        if (!movieRepository.existsById(id)) {
            log.error(String.format("Could not find movie by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find movie by id %s.", id));
        }
        movie.setId(id);
        movieRepository.save(movie);
    }

    @CacheEvict(value = "libraryCache", key = "#id")
    public void deleteMovie(String id) {
        if (!movieRepository.existsById(id)) {
            log.error(String.format("Could not find movie by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find movie by id %s.", id));
        }
        movieRepository.deleteById(id);
    }
}
