package com.gmail.eugeneknysh21.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private Long id;

    @NotBlank(message = "Email can`t be empty.")
    @Email(message = "Email doesn`t match pattern (example@example.com).")
    private String email;

    @NotBlank(message = "Name can`t be empty.")
    @Size(max = 25, message = "Name is more than 25 characters.")
    private String firstName;

    @NotBlank(message = "Surname can`t be empty.")
    @Size(max = 25, message = "Surname is more than 25 characters.")
    private String lastName;

    @NotBlank(message = "Alias can`t be empty.")
    @Size(max = 25, message = "Alias is more than 25 characters.")
    private String alias;
}