package com.ana_luiza.project_tracker.dto;

public class AuthenticationResponse {

    private final String token; // Token JWT gerado

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}