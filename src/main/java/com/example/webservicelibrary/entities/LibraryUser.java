package com.example.webservicelibrary.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class LibraryUser implements Serializable {

    private static final long serialVersionUID = -846068740697687411L;
    @Id
    private String id;
    @NotEmpty(message = "Firstname can't be empty")
    @Size(min = 2, max = 10)
    private String firstname;
    @NotEmpty(message = "Lastname can't be empty")
    @Size(min = 2, max = 15)
    private String lastname;
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthdate;
    @Email
    @Indexed(unique = true)
    private String email;
    @Pattern(regexp = "([0-9]){2,4}-([0-9]){5,8}", message = "Phone number not valid")
    private String phone;
    @NotBlank(message = "Username must contain a value")
    @Indexed(unique = true)
    @Size(min = 2, max = 10, message = "Username invalid")
    private String username;
    @NotBlank(message = "Password must contain a value")
    private String password;
    private List<String> acl;
    private List<Book> borrowedBooks;
}
