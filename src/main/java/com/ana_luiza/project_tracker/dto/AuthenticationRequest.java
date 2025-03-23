package com.ana_luiza.project_tracker.dto;

import lombok.*;

@Getter @Setter
public class AuthenticationRequest {
    private String email;
    private String senha;

}