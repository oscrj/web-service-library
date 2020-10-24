package com.example.webservicelibrary.repositories;

import com.example.webservicelibrary.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

}
