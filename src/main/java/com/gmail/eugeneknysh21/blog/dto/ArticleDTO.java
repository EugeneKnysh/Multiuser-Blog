package com.gmail.eugeneknysh21.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;

    private UserDTO author;

    @NotBlank(message = "Title can`t be empty.")
    @Size(min = 1, max = 50, message = "Name is more than 100 characters.")
    private String title;

    @NotBlank(message = "Section can`t be empty.")
    @Size(min = 1, max = 25, message = "Section is more than 25 characters.")
    private String section;

    @NotBlank(message = "Anons can`t be empty.")
    @Size(min = 1, max = 255, message = "Anons is more than 100 characters.")
    private String anons;

    @NotBlank(message = "Text can`t be empty.")
    @Size(min = 1)
    private String fullText;

    private ZonedDateTime createdDate;
    private int views;
}
