package com.example.webservicelibrary.enteties;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Data
@Builder
public class LibraryUser {
    @Id
    private String id;
    private String firstname;
    private String lastName;
    private LocalDate birthdate;
    @Indexed(unique = true)
    private String email;
    private String phone;
    @Indexed(unique = true)
    private String username;
    private String password;
    private boolean admin;

    // private List<Book> borrowedBooks = new
}
