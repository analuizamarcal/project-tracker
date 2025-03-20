package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// Cria usuário
	public Usuario criarUsuario(Usuario usuario) {
		// Verifica se o e-mail já está cadastrado
	    try {
	        buscarUsuarioPorEmail(usuario.getEmail());
	        throw new RuntimeException("Email já cadastrado!");
	    } catch (RuntimeException e) {
	        if (!e.getMessage().equals("Usuário não encontrado!")) {
	            throw e;
	        }
	    }

	 // Criptografa a senha antes de salvar
	    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
	    
	    // Salva e retorna o usuário criado
	    return usuarioRepository.save(usuario);
	}

	// Lista usuários
	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}
	
	// Busca usuário pelo ID
	public Usuario buscarUsuarioPorId(Long id) {
		// Retorna usuário caso encontrado
	    return usuarioRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
	}
	
	// Busca usuário pelo email
	public Usuario buscarUsuarioPorEmail(String email) {
	    return usuarioRepository.findByEmail(email)
	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
	}
	
	// Atualiza usuário
	public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
	    Usuario usuario = buscarUsuarioPorId(id);
	    usuario.setNome(usuarioAtualizado.getNome()); // Atualiza nome do usuário
	    usuario.setEmail(usuarioAtualizado.getEmail()); // Atualiza email do usuário
	    usuario.setRole(usuarioAtualizado.getRole()); // Atualiza role do usuário

	    // Salva novas informações do usuário
	    return usuarioRepository.save(usuario);
	}
	
	// Altera senha do usuário
	public void alterarSenha(Long id, String novaSenha) {
	    Usuario usuario = buscarUsuarioPorId(id);
	    usuario.setSenha(passwordEncoder.encode(novaSenha)); // Atualiza senha do usuário
	    
	 // Salva nova senha do usuário
	    usuarioRepository.save(usuario); 
	}


	// Exclui usuário
	public void excluirUsuario(Long id) {
	    Usuario usuario = buscarUsuarioPorId(id);
	    usuarioRepository.delete(usuario);
	}
}
