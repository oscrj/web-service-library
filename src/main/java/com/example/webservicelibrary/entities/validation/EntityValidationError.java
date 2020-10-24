package com.example.webservicelibrary.entities.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityValidationError {
    private String field;
    private String message;
    private String rejectedValue;
}
