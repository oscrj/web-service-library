package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.repositories.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryUserService {

    private final LibraryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "libraryCache")
    public List<LibraryUser> findAllUsers(String name, boolean sortOnLastname) {
        log.info("Request to find all users.");
        log.info("Fresh User data...");

        var users = userRepository.findAll();
        if (name != null) {
            users = users.stream()
                    .filter(user -> user.getFirstname().startsWith(name) || user.getLastname().startsWith(name))
                    .collect(Collectors.toList());
        }
        if (sortOnLastname){
            users.sort(Comparator.comparing(LibraryUser::getLastname));
        }
        return users;
    }

    @Cacheable(value = "libraryCache", key = "#id")
    public LibraryUser findById(String id) {
        if(!userRepository.existsById(id)) log.error(String.format("User with this id %s. , could not be found", id));
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with this id %s. , could not be found", id)));
    }

    public LibraryUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with this username %s. , could not be found", username)));
    }

    @CachePut(value = "libraryCache", key = "#result.id")
    public LibraryUser saveUser(LibraryUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt users password.
        return userRepository.save(user);
    }

    @CachePut(value = "libraryCache", key = "#id")
    public void updateUser(String id, LibraryUser user) {
        // check if current logged in user is ADMIN.
        var isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals("ROLE_ADMIN"));
        // check if logged in user is the user that will be updated.
        var isCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName().toLowerCase()
                .equals(user.getUsername().toLowerCase());

        if (!isAdmin && !isCurrentUser) {
            log.error("Logged in user "+ SecurityContextHolder.getContext().getAuthentication().getName()
                    +" tried to update another user.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can only update your own details.");
        }

        if(!userRepository.existsById(id)){
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }

        // Prevent encoding on encoded password.....
        if (user.getPassword().length() <= 16) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setId(id);
        userRepository.save(user);
    }

    @CacheEvict(value = "libraryCache", key = "#id")
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            log.error(String.format("User with this id %s. , could not be found", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with this id %s. , could not be found", id));
        }
        userRepository.deleteById(id);
    }

}
