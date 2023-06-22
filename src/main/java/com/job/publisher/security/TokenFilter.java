package com.job.publisher.security;

import com.job.publisher.config.APIKeyProperties;
import com.job.publisher.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private APIKeyProperties apiKeyProperties;

    public TokenFilter(JwtTokenProvider jwtTokenProvider, APIKeyProperties apiKeyProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.apiKeyProperties = apiKeyProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromHeader(request.getHeader("Authorization"));
            String apiKeyValue = request.getHeader("X-API-KEY");

            if (StringUtils.hasText(token)) {
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else if (apiKeyProperties.getApiKey().equals(apiKeyValue)) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(apiKeyValue, null, Collections.singletonList(new SimpleGrantedAuthority("USER")));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token is invalid", HttpStatus.UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }


    private String extractTokenFromHeader(String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}