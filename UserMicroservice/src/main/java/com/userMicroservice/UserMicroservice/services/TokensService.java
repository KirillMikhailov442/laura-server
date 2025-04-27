package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.dto.TokensCreateDTO;
import com.userMicroservice.UserMicroservice.exceptions.NotFound;
import com.userMicroservice.UserMicroservice.models.Tokens;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.repositories.TokensRepository;
import com.userMicroservice.UserMicroservice.utils.jwt.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokensService {
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private JWTUtils jwtUtils;

    public Tokens findTokenById(long id){
        return tokensRepository.findById(id)
                .orElseThrow(()-> new NotFound("Token not found with such id"));
    }

    public Tokens findByRefresh(String refresh){
        return tokensRepository.findByRefreshToken(refresh)
                .orElseThrow(()-> new NotFound("Tokens not found with such refresh token"));
    }

    public Tokens findByUserId(long userId){
        List<Tokens> tokens = tokensRepository.findAllByUserIdId(userId);
        if(tokens.isEmpty()) throw new NotFound("Tokens not found with such user id");
        return tokens.getFirst();
    }

    public String getRefreshFromTokens(long id){
        return findTokenById(id).getRefreshToken();
    }

    public String getIPFromTokens(long id){
        return findTokenById(id).getIP();
    }

    public String getUserAgentFromTokens(long id){
        return findTokenById(id).getUserAgent();
    }

    public void createToken(TokensCreateDTO dto, User user){
        Tokens token = Tokens.builder().userId(user).refreshToken(dto.getRefresh()).IP(dto.getIP()).userAgent(dto.getUserAgent()).build();
        tokensRepository.save(token);
    }

    public Tokens updateToken(TokensCreateDTO dto, long tokenId){
        Tokens token = findTokenById(tokenId);

        if(dto.getIP() != null) token.setIP(dto.getIP());
        if(dto.getRefresh() != null) token.setRefreshToken(dto.getRefresh());
        if(dto.getUserAgent() != null) token.setUserAgent(dto.getUserAgent());
        return tokensRepository.save(token);
    }

    public Tokens updateRefreshToken(String refresh, long tokenId){
        Tokens token = findTokenById(tokenId);
        token.setRefreshToken(refresh);
        return tokensRepository.save(token);
    }

    public void deleteTokenById(long tokenId){
        tokensRepository.deleteById(tokenId);
    }

}
