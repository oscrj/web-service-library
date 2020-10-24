package com.example.webservicelibrary.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class Movies implements Serializable {

    private static final long serialVersionUID = -8202903518238947328L;
    @Id
    private String id;
    @NotEmpty(message = "Title must contain between 2 - 32 characters.")
    @Size(min = 2, max = 32)
    private String title;
    @NotEmpty(message = "Director must contain between 2 - 20 characters.")
    @Size(min = 2, max = 20)
    private String Director;
    @NotEmpty(message = "Enter the movies genre")
    private String genre;
    private double rating;
}
