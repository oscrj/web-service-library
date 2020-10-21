package com.example.webservicelibrary.enteties;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Book {

    @Id
    private String id;

}
