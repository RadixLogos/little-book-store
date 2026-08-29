package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.LoginRequestDTO;
import com.radixlogos.littlebookstore.dto.LoginResponseDTO;
import com.radixlogos.littlebookstore.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
        var response  = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }


}
