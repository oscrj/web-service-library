package com.example.webservicelibrary.repositories;

import com.example.webservicelibrary.entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
