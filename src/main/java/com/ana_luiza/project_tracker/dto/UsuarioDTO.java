package com.ana_luiza.project_tracker.dto;

import lombok.*;

@Getter @Setter
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role; // Se for um enum, pode ser String
}
