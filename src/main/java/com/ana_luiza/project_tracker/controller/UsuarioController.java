package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.UsuarioDTO;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Criar usuário (somente ADMIN pode criar)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String criarUsuario(@RequestBody Usuario usuario) {
        usuarioService.criarUsuario(usuario);
        return "Usuário criado com sucesso!";
    }

    // Listar todos os usuários (somente ADMIN pode acessar)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Buscar usuário por ID (somente ADMIN ou o próprio usuário podem acessar)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public Usuario buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    // Atualizar usuário (somente o ADMIN ou o próprio usuário podem editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        return usuarioService.atualizarUsuario(id, usuarioAtualizado);
    }

    // Alterar senha (somente o próprio usuário pode alterar)
    @PutMapping("/{id}/senha")
    @PreAuthorize("#id == principal.id")
    public String alterarSenha(@PathVariable Long id, @RequestBody String novaSenha) {
        usuarioService.alterarSenha(id, novaSenha);
        return "Senha alterada com sucesso!";
    }

    // Excluir usuário (somente ADMIN pode excluir)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return "Usuário excluído com sucesso!";
    }
}
