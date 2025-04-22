package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.dto.TokensDTO;
import com.userMicroservice.UserMicroservice.exceptions.NotFound;
import com.userMicroservice.UserMicroservice.models.Tokens;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.repositories.TokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokensService {
    @Autowired
    private TokensRepository tokensRepository;

    public Tokens findTokenById(long id){
        return tokensRepository.findById(id)
                .orElseThrow(()-> new NotFound("Token not found with such id"));
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

    public Tokens createToken(TokensDTO dto, User user){
        Tokens token = Tokens.builder().userId(user).refreshToken(dto.getRefresh()).IP(dto.getIP()).userAgent(dto.getUserAgent()).build();
        return tokensRepository.save(token);
    }

    public Tokens updateToken(TokensDTO dto, long tokenId){
        Tokens token = findTokenById(tokenId);

        if(dto.getIP() != null) token.setIP(dto.getIP());
        if(dto.getRefresh() != null) token.setRefreshToken(dto.getRefresh());
        if(dto.getUserAgent() != null) token.setUserAgent(dto.getUserAgent());
        return tokensRepository.save(token);
    }

    public void deleteTokenById(long tokenId){
        tokensRepository.deleteById(tokenId);
    }
}
