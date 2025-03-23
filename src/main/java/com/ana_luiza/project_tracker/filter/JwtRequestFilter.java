package com.ana_luiza.project_tracker.filter;

import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String SECRET_KEY;
    private final UsuarioRepository usuarioRepository; // Repositório de usuários
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(UsuarioRepository usuarioRepository) {
        // Carrega as variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.load();
        this.SECRET_KEY = dotenv.get("JWT_SECRET"); // Obtém a chave secreta do .env
		this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Remove "Bearer " para obter o token

            try {
                // Verifica e decodifica o token JWT
                Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .build()
                        .verify(jwt);

                String email = decodedJWT.getSubject(); // Extrai o email do token
                logger.info("Usuário autenticado: " + email);

                // Extrai a role do token
                String role = decodedJWT.getClaim("role").asString();
                logger.info("Role extraída do token: " + role);

                // Verifica se a role já contém o prefixo "ROLE_"
                String roleComPrefixo = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                logger.info("Role com prefixo: " + roleComPrefixo);

                // Converte a role para uma autoridade do Spring Security
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleComPrefixo);

                // Carrega o usuário completo a partir do email
                Usuario usuario = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                // Cria um objeto de autenticação com o usuário completo como principal
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, Collections.singletonList(authority));

                // Define o objeto de autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.info("Autenticação configurada no SecurityContext.");
            } catch (JWTVerificationException e) {
                // Em caso de token inválido, você pode logar o erro ou retornar uma resposta de erro
                logger.error("Token JWT inválido: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            logger.warn("Cabeçalho Authorization ausente ou inválido.");
        }

        chain.doFilter(request, response); // Continua o processamento da requisição
    }
}