package com.userMicroservice.UserMicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class LoginDTO {
    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    private String email;

    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    private String password;
}
