package com.ana_luiza.project_tracker.security;

import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;
    private final UsuarioRepository usuarioRepository;
    private static final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos

    public JwtService(UsuarioRepository usuarioRepository) {
    	this.usuarioRepository = usuarioRepository;
    	
        // Carrega as variáveis do .env
        Dotenv dotenv = Dotenv.load();
        this.secretKey = dotenv.get("JWT_SECRET");

        if (this.secretKey == null || this.secretKey.isEmpty()) {
            throw new IllegalStateException("A chave JWT_SECRET não está definida no .env");
        }
    }

    public String generateToken(String email, Long userId, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null; // Token inválido
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername());
    }
    
    public Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            return usuario != null ? usuario.getId() : null;
        }
        
        return null;
    }
}
