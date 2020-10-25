package com.example.webservicelibrary.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class Movie implements Serializable {

    private static final long serialVersionUID = -8202903518238947328L;
    @Id
    private String id;
    @NotEmpty(message = "Title must contain between 2 - 64 characters.")
    @Size(min = 2, max = 64)
    private String title;
    @NotEmpty(message = "Director must contain between 2 - 20 characters.")
    @Size(min = 2, max = 20)
    private String director;
    @NotEmpty(message = "Enter the movies genre")
    private String genre;
    @NotNull
    private double rating;
    @NotNull
    private int publishedYear;
    private boolean isAvailable;
}
