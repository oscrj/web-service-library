package com.example.webservicelibrary.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class Newspaper implements Serializable {

    // not using this entity... repeating code to achieve nothing.
    private static final long serialVersionUID = -3028531895261196552L;
    @Id
    private String id;
    @NotEmpty(message = "Title must contain between 2 - 32 characters.")
    @Size(min = 2, max = 32)
    private String title;
    @NotEmpty(message = "Publisher must contain between 2 - 20 characters.")
    @Size(min = 2, max = 20)
    private String publisher;
    private boolean isAvailable;
}
