package com.userMicroservice.UserMicroservice.dto;

import com.userMicroservice.UserMicroservice.constants.NicknameConstants;
import com.userMicroservice.UserMicroservice.constants.PasswordConstants;
import com.userMicroservice.UserMicroservice.constants.Regexps;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserReqDTO {

    @NotNull(message = "firstName is mandatory")
    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    private String lastName;

    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Incorrect email")
    private String email;

    @NotNull(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = PasswordConstants.MIN_LENGTH_PASSWORD, max = PasswordConstants.MAX_LENGTH_PASSWORD, message = "Password length should be " + PasswordConstants.MIN_LENGTH_PASSWORD + " to " + PasswordConstants.MAX_LENGTH_PASSWORD + " characters")
    private String password;

    @NotBlank(message = "Nickname is mandatory")
    @Size(min = NicknameConstants.MIN_LENGTH_NICKNAME, max = NicknameConstants.MAX_LENGTH_NICKNAME, message = "Password length should be " + NicknameConstants.MIN_LENGTH_NICKNAME + " to " + NicknameConstants.MAX_LENGTH_NICKNAME + " characters")
    @Pattern(regexp = Regexps.regexNickname, message = "The nickname should start with @")
    private String nickname;

    @Override
    public String toString() {
        return String.format("firstname: %s \n lastname: %s \n email: %s \n nickname: %s \n password: %s", firstName, lastName, email, nickname, password);

    }
}
