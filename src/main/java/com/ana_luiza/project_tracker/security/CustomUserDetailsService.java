package com.ana_luiza.project_tracker.security;

import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getSenha())
            .roles(usuario.getRole().name()) // Certifique-se de que Role é um ENUM válido
            .build();
    }
}
