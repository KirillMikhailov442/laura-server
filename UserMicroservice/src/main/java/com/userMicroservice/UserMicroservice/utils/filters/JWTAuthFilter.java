package com.userMicroservice.UserMicroservice.utils.filters;

import com.userMicroservice.UserMicroservice.interfaces.TokensTypes;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.services.TokensService;
import com.userMicroservice.UserMicroservice.services.UserService;
import com.userMicroservice.UserMicroservice.utils.jwt.JWTUtils;
import com.userMicroservice.UserMicroservice.utils.jwt.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    TokensService tokensService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String accessToken = null;
        String refreshToken = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            accessToken = authHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(accessToken, TokensTypes.ACCESS);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserPrincipal userPrincipal = userService.loadUserByUsername(username);
            if(!jwtUtils.validateToken(accessToken, TokensTypes.ACCESS, userPrincipal)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                if(jwtUtils.validateToken(refreshToken, TokensTypes.REFRESH, userPrincipal)){
                    User user = userService.findUserById(Long.parseLong(jwtUtils.getUsernameFromToken(refreshToken, TokensTypes.REFRESH)));
                    String newRefreshToken = jwtUtils.generateRefreshToken(new UserPrincipal(user));
                    tokensService.updateRefreshToken(newRefreshToken, tokensService.findByRefresh(refreshToken).getId());

                    userPrincipal = userService.loadUserByUsername(String.valueOf(user.getId()));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
