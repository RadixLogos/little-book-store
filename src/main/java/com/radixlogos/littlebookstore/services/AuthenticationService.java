package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.entities.User;
import com.radixlogos.littlebookstore.dto.LoginRequestDTO;
import com.radixlogos.littlebookstore.dto.LoginResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO authenticate(LoginRequestDTO request){
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                                request.username(),request.password());

        Authentication authenticated = authenticationManager.authenticate(authentication);
        String token = jwtService.generateToken((User) Objects.requireNonNull(authenticated.getPrincipal()));

        return new LoginResponseDTO(token);
    }
}
