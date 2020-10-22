package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // instead of @Autowired on LibraryUserRepository, make LibraryUserRepository final..
public class LibraryUserService {

    private final LibraryUserRepository userRepository;

    public List<LibraryUser> findAllUsers() {
        log.info("Request to find all users.");

        var users = userRepository.findAll();
        return users;
    }

    public LibraryUser findById(String id) {
        if(!userRepository.existsById(id)) log.error(String.format("User with this id %s. , could not be found", id));
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with this id %s. , could not be found", id)));
    }

    public LibraryUser saveUser(LibraryUser user) {
        return userRepository.save(user);
    }

    public void updateUser(String id, LibraryUser user) {
        if(!userRepository.existsById(id)){
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }

        user.setId(id);
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        userRepository.deleteById(id);
    }

}
