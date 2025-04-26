package com.userMicroservice.UserMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokensCreateDTO {
    private String refresh;
    private String IP;
    private String userAgent;
}
