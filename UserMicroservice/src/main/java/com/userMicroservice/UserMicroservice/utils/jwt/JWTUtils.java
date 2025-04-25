package com.userMicroservice.UserMicroservice.utils.jwt;

import com.userMicroservice.UserMicroservice.interfaces.TokensTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    @Value("${token.access.secret}")
    private String accessKey;

    @Value("${token.access.lifetime}")
    private int accessLifeTime;

    @Value("${token.refresh.secret}")
    private String refreshKey;

    @Value("${token.refresh.lifetime}")
    private int refreshLifeTime;

    private String generateToken(UserPrincipal user, Map<String, Object> extraClaims, String key, int lifeTime){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateToken(UserPrincipal user, String key, int lifeTime){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserPrincipal user, Map<String, Object> extraClaims){
        return generateToken(user, extraClaims, accessKey, accessLifeTime);
    }

    public String generateAccessToken(UserPrincipal user){
        return generateToken(user, accessKey, accessLifeTime);
    }

    public String generateRefreshToken(UserPrincipal user, Map<String, Object> extraClaims){
        return generateToken(user, extraClaims, refreshKey, refreshLifeTime);
    }

    public String generateRefreshToken(UserPrincipal user){
        return generateToken(user, refreshKey, refreshLifeTime);
    }

    public Claims getClaimsFromToken(String token, TokensTypes tokenType){
        String key;
        if(tokenType.equals(TokensTypes.ACCESS))
            key = accessKey;
        else key = refreshKey;

        return Jwts.parser().setSigningKey(getKey(key)).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsTFunction, TokensTypes tokensTypes){
        final Claims claims = getClaimsFromToken(token, tokensTypes);
        return claimsTFunction.apply(claims);
    }

    public Date getExpirationDateFromToken(String token, TokensTypes tokensTypes){
        return getClaimsFromToken(token, tokensTypes).getExpiration();
    }

    public boolean isTokenExpired(String token, TokensTypes tokensTypes){
        return  getExpirationDateFromToken(token, tokensTypes).before(new Date());
    }

    public String getUsernameFromToken(String token, TokensTypes tokensTypes){
        return getClaimsFromToken(token, tokensTypes).getSubject();
    }

    public boolean validateToken(String token, TokensTypes tokensTypes, UserPrincipal user){
        try{
            final String username = getUsernameFromToken(token, tokensTypes);
            return (username.equals(user.getUsername()) && isTokenExpired(token, tokensTypes));
        } catch (Exception e) {
            return false;
        }
    }

    private Key getKey(String key){
        byte[] KeyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
}
