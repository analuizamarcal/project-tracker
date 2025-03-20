package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	// Criar usuário
	@PostMapping
	public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
		Usuario novoUsuario = usuarioService.criarUsuario(usuario);
		return ResponseEntity.ok(novoUsuario);
	}
	
	// Listar usuários
	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioService.listarUsuarios();
		return ResponseEntity.ok(usuarios);
	}
	
	// Buscar usuário por ID
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
	}
	
	// Atualizar usuário
	@PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }
	
	// Alterar senha
	@PatchMapping("/{id}/senha")
	public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody String novaSenha) {
		usuarioService.alterarSenha(id, novaSenha);
		return ResponseEntity.noContent().build();
	}
	
	// Excluir usuario
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
		usuarioService.excluirUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
