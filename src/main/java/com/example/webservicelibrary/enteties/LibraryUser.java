package com.example.webservicelibrary.enteties;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class LibraryUser implements Serializable {

    private static final long serialVersionUID = -846068740697687411L;
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
    private List<Book> borrowedBook;

}
