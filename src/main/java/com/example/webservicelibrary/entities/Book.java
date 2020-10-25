package com.example.webservicelibrary.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Builder
public class Book implements Serializable {

    private static final long serialVersionUID = -5550870321107712437L;

    @Id
    private String id;
    @NotEmpty(message = "Title must contain between 2 - 64 characters.")
    @Size(min = 2, max = 64)
    private String title;
    @NotEmpty(message = "Author must contain between 2 - 32 characters.")
    @Size(min = 2, max = 32)
    private String author;
    @NotEmpty(message = "Enter the books genre")
    private String genre;
    @NotNull
    private int publishedYear;
    private boolean isAvailable;

}
