package com.userMicroservice.UserMicroservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokensDTO {
    private String accessToken;
}
