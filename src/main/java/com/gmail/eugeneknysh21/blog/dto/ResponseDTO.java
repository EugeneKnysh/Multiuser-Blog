package com.gmail.eugeneknysh21.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private int responseStatus;
    private String message;
}
