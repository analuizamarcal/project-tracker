package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Usuario;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioViewDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;

    // Método para converter de Usuario para UsuarioViewDTO
    public static UsuarioViewDTO fromUsuario(Usuario usuario) {
        return new UsuarioViewDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getRole().name() // Converte o enum Role para String
        );
    }
}
