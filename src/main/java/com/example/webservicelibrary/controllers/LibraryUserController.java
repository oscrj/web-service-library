package com.example.webservicelibrary.controllers;

import com.example.webservicelibrary.entities.LibraryUser;
import com.example.webservicelibrary.services.LibraryUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //  REST API Endpoints.
@RequestMapping("/library-api/v1/users")
@Slf4j
public class LibraryUserController {

    @Autowired
    private LibraryUserService userService;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<LibraryUser>> findAllLibraryUsers(@RequestParam(required = false) String name) {
        // use name to later add functionality to search on user name.
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<LibraryUser> findLibraryUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<LibraryUser> saveLibraryUser(@Validated @RequestBody LibraryUser user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLibraryUser(@PathVariable String id, @RequestBody LibraryUser user) {
        userService.updateUser(id, user);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLibraryUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
