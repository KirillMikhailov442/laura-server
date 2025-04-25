package com.userMicroservice.UserMicroservice.dto;

import com.userMicroservice.UserMicroservice.constants.NicknameConstants;
import com.userMicroservice.UserMicroservice.constants.Regexps;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserUpdateDTO {
    private String firstName;

    private String lastName;

    @Email(message = "Incorrect email")
    private String email;

    @Size(min = NicknameConstants.MIN_LENGTH_NICKNAME, max = NicknameConstants.MAX_LENGTH_NICKNAME, message = "Nickname length should be " + NicknameConstants.MIN_LENGTH_NICKNAME + " to " + NicknameConstants.MAX_LENGTH_NICKNAME + " characters")
    @Pattern(regexp = Regexps.regexNickname, message = "The nickname should start with @")
    private String nickname;
}
