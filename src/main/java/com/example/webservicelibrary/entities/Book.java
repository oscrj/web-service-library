package com.example.webservicelibrary.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
public class Book implements Serializable {

    private static final long serialVersionUID = -5550870321107712437L;

    @Id
    private String id;
    private boolean borrowed;

}
