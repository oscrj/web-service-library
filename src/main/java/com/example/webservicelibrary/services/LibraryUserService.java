package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // instead of @Autowired on LibraryUserRepository, make LibraryUserRepository final..
public class LibraryUserService {

    private final LibraryUserRepository userRepository;

    @Cacheable(value = "libraryCache")
    public List<LibraryUser> findAllUsers() {
        log.info("Request to find all users.");
        log.info("Fresh data...");

        var users = userRepository.findAll();
        return users;
    }

    @Cacheable(value = "libraryCache", key = "#id")
    public LibraryUser findById(String id) {
        if(!userRepository.existsById(id)) log.error(String.format("User with this id %s. , could not be found", id));
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with this id %s. , could not be found", id)));
    }

    @CachePut(value = "libraryCache", key = "#result.id")
    public LibraryUser saveUser(@Validated LibraryUser user) {
        return userRepository.save(user);
    }

    @CachePut(value = "libraryCache", key = "#id")
    public void updateUser(String id, LibraryUser user) {
        if(!userRepository.existsById(id)){
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }

        user.setId(id);
        userRepository.save(user);
    }

    @CacheEvict(value = "libraryCache")
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        userRepository.deleteById(id);
    }

}
