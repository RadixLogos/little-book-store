package com.radixlogos.littlebookstore.config;


import com.radixlogos.littlebookstore.entities.User;
import com.radixlogos.littlebookstore.services.JWTService;
import com.radixlogos.littlebookstore.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JWTService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {


        String authHeader = request.getHeader("Authorization");

        if(authHeader == null  || authHeader.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }
        if(authHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null){
            String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token);

            UserDetails user = userDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(token,(User) user)){
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    filterChain.doFilter(request,response);
    } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Token expirado\"}"
            );
        }
    }
}
