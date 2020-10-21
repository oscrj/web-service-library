package com.example.webservicelibrary.repositories;

import com.example.webservicelibrary.enteties.LibraryUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryUserRepository  extends MongoRepository<LibraryUser , String> {
}
