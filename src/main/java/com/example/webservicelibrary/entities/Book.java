package com.example.webservicelibrary.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class Book implements Serializable {

    private static final long serialVersionUID = -5550870321107712437L;

    @Id
    private String id;
    @NotEmpty(message = "Title must contain between 2 - 32 characters.")
    @Size(min = 2, max = 32)
    private String title;
    @NotEmpty(message = "Author must contain between 2 - 20 characters.")
    @Size(min = 2, max = 20)
    private String Author;
    @NotEmpty(message = "Enter the books genre")
    private String genre;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate publishedDate;
    private boolean isAvailable;

}
