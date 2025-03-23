package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.UsuarioCreateDTO;
import com.ana_luiza.project_tracker.dto.UsuarioViewDTO;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
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
    private PasswordEncoder passwordEncoder; // Para criptografar senhas (caso esteja usando segurança)

    // Método para buscar todos os usuários
    public List<UsuarioViewDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioViewDTO::fromUsuario) // Converte cada Usuario para UsuarioDTO
                .collect(Collectors.toList());
    }

    // Método para buscar um usuário por ID
    public UsuarioViewDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return UsuarioViewDTO.fromUsuario(usuario); // Converte para UsuarioDTO
    }

    // Criar um novo usuário
    public UsuarioViewDTO create(UsuarioCreateDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRole(Usuario.Role.valueOf(usuarioDTO.getRole())); // Define a role
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha())); // Criptografa a senha

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return UsuarioViewDTO.fromUsuario(novoUsuario); // Converte para UsuarioViewDTO
    }

    // Atualizar um usuário existente (sem alterar senha diretamente)
    public UsuarioViewDTO update(Long id, UsuarioCreateDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRole(Usuario.Role.valueOf(usuarioDTO.getRole()));

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioViewDTO.fromUsuario(usuarioAtualizado); // Converte para UsuarioViewDTO
    }
    
    // Alterar senha do usuário
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se a senha atual está correta
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        // Atualiza a senha com a nova senha criptografada
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    // Método para deletar um usuário
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }
}