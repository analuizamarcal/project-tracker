package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.AuthenticationRequest;
import com.ana_luiza.project_tracker.dto.AuthenticationResponse;
import com.ana_luiza.project_tracker.util.JwtUtil;
import com.ana_luiza.project_tracker.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // Autentica o usuário usando email e senha
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getSenha())
        );

        // Carrega os detalhes do usuário
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        // Gera o token JWT
        final String jwt = jwtUtil.generateToken(userDetails);

        // Retorna o token
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}