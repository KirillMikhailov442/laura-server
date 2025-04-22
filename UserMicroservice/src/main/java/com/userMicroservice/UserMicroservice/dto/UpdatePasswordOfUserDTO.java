package com.userMicroservice.UserMicroservice.dto;

import com.userMicroservice.UserMicroservice.constants.PasswordConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordOfUserDTO {
    @NotNull(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = PasswordConstants.MIN_LENGTH_PASSWORD, max = PasswordConstants.MAX_LENGTH_PASSWORD, message = "Old password length should be " + PasswordConstants.MIN_LENGTH_PASSWORD + " to " + PasswordConstants.MAX_LENGTH_PASSWORD + " characters")
    private String oldPassword;

    @NotNull(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = PasswordConstants.MIN_LENGTH_PASSWORD, max = PasswordConstants.MAX_LENGTH_PASSWORD, message = "New password length should be " + PasswordConstants.MIN_LENGTH_PASSWORD + " to " + PasswordConstants.MAX_LENGTH_PASSWORD + " characters")
    private String newPassword;
}
