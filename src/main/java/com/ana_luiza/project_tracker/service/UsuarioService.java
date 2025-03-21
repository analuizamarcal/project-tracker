package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.UsuarioDTO;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.model.Usuario.Role;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import com.ana_luiza.project_tracker.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
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

	public List<UsuarioDTO> listarUsuarios() {
	    return usuarioRepository.findAll().stream()
	        .map(usuario -> {
	        	UsuarioDTO dto = new UsuarioDTO();
	        	dto.setId(usuario.getId());
	        	dto.setNome(usuario.getNome());
	        	dto.setEmail(usuario.getEmail());
	        	dto.setRole(usuario.getRole().name()); // Se for um enum, converter para String
	        	return dto;
	        })
	    .collect(Collectors.toList());
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
	
	// Atualiza usuário com validação de permissão
	public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
		Long authenticatedUserId = jwtService.getAuthenticatedUserId(); // Obtém o ID do usuário autenticado
		Usuario authenticatedUser = buscarUsuarioPorId(authenticatedUserId); // Busca o usuário autenticado

		boolean isAdmin = authenticatedUser.getRole().equals(Role.ADMIN);
		boolean isOwner = authenticatedUserId.equals(id);

		if (!isAdmin && !isOwner) {
			throw new RuntimeException("Você não tem permissão para editar este usuário.");
		}

		Usuario usuario = buscarUsuarioPorId(id);
		usuario.setNome(usuarioAtualizado.getNome());
		usuario.setEmail(usuarioAtualizado.getEmail());
		usuario.setRole(usuarioAtualizado.getRole());

		return usuarioRepository.save(usuario);
	}
	
	// Altera senha do usuário com validação de permissão
	public void alterarSenha(Long id, String novaSenha) {
		Long authenticatedUserId = jwtService.getAuthenticatedUserId();
		boolean isOwner = authenticatedUserId.equals(id);
	
		if (!isOwner) {
		    throw new RuntimeException("Você não tem permissão para alterar esta senha.");
		}
	
		Usuario usuario = buscarUsuarioPorId(id);
		usuario.setSenha(passwordEncoder.encode(novaSenha));
		usuarioRepository.save(usuario);
	}

	// Exclui usuário com validação de permissão
	public void excluirUsuario(Long id) {
		Long authenticatedUserId = jwtService.getAuthenticatedUserId();
		Usuario authenticatedUser = buscarUsuarioPorId(authenticatedUserId);

		boolean isAdmin = authenticatedUser.getRole().equals(Role.ADMIN);
		boolean isOwner = authenticatedUserId.equals(id);

		if (!isAdmin && !isOwner) {
		    throw new RuntimeException("Você não tem permissão para excluir este usuário.");
		}

		Usuario usuario = buscarUsuarioPorId(id);
		usuarioRepository.delete(usuario);
	}
}
