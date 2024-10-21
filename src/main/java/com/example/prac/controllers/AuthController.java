package com.example.prac.controllers;

import com.example.prac.DTO.auth.AuthenticationRequest;
import com.example.prac.DTO.auth.AuthenticationResponse;
import com.example.prac.service.auth.AuthenticationService;
import com.example.prac.DTO.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        return ResponseEntity.ok(authenticationResponse);
    }
    
    @PostMapping("/authenticate") // FIXME: возвращает 500 при несуществующем юзере или неправильных кредах
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @GetMapping("/verify-token") // FIXME: токен не проверяется
    public ResponseEntity<?> checkToken() {
        return ResponseEntity.ok("Token is valid");
    }

}
