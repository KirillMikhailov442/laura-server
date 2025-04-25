package com.userMicroservice.UserMicroservice.utils.filters;

import com.userMicroservice.UserMicroservice.interfaces.TokensTypes;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            System.out.println("seccuess");
            token = authHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(token, TokensTypes.ACCESS);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserPrincipal userPrincipal = userService.loadUserByUsername(username);
            System.out.println(userPrincipal.getAuthorities());
            if(!jwtUtils.validateToken(token, TokensTypes.ACCESS, userPrincipal)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
