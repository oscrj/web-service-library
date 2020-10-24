package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryUserService {

    private final LibraryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //@Cacheable(value = "libraryCache")
    public List<LibraryUser> findAllUsers() {
        log.info("Request to find all users.");
        log.info("Fresh data...");

        var users = userRepository.findAll();
        return users;
    }

    //@Cacheable(value = "libraryCache", key = "#id")
    public LibraryUser findById(String id) {
        if(!userRepository.existsById(id)) log.error(String.format("User with this id %s. , could not be found", id));
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with this id %s. , could not be found", id)));
    }

    //@CachePut(value = "libraryCache", key = "#result.id")
    public LibraryUser saveUser(LibraryUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt users password.
        return userRepository.save(user);
    }

    //@CachePut(value = "libraryCache", key = "#id")
    public void updateUser(String id, LibraryUser user) {
        if(!userRepository.existsById(id)){
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //@CacheEvict(value = "libraryCache", key = "#id")
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        userRepository.deleteById(id);
    }

}
