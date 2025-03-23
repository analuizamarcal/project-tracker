package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Usuario;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {
    private String nome;
    private String email;
    private String senha;
    private String role; // Definição do papel do usuário

    // Método para converter de UsuarioCreateDTO para Usuario
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha); // A senha deve ser hashada antes de salvar
        usuario.setRole(Usuario.Role.valueOf(this.role)); // Converte a String para o enum Role
        return usuario;
    }
}
