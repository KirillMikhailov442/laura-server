package com.userMicroservice.UserMicroservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class BaseURL {

    private final String scheme;
    private final String host;
    private final String baseURL;

    public BaseURL(HttpServletRequest request){
        this.scheme = request.getScheme();
        this.host = request.getHeader("Host");
        this.baseURL = scheme + "://" + host;
    }
}
