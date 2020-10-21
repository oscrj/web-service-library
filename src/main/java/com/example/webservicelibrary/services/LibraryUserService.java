package com.example.webservicelibrary.services;

import com.example.webservicelibrary.enteties.LibraryUser;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryUserService {

    private final LibraryUserRepository userRepository;

    public List<LibraryUser> findAllUsers() {
        var users = userRepository.findAll();
        return users;
    }
}
