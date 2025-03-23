package com.ana_luiza.project_tracker.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    private final String SECRET_KEY;
    private final Algorithm algorithm;

    public JwtUtil() {
        // Carrega as variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.configure().load();
        this.SECRET_KEY = dotenv.get("JWT_SECRET"); // Obtém a chave secreta do .env
        this.algorithm = Algorithm.HMAC256(SECRET_KEY); // Configura o algoritmo de assinatura
    }

    // Gera o token JWT
    public String generateToken(UserDetails userDetails) {
        // Extrai a role do usuário e adiciona o prefixo "ROLE_"
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        // Garante que a role tenha o prefixo "ROLE_"
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return JWT.create()
                .withSubject(userDetails.getUsername()) // Usa o email como subject
                .withClaim("role", role) // Adiciona a role ao token
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .sign(algorithm); // Assina o token com a chave secreta
    }

    // Valida o token JWT
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            // Verifica se o token é válido e se o subject (email) corresponde ao usuário
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String username = decodedJWT.getSubject();
            return username.equals(userDetails.getUsername()) && !isTokenExpired(decodedJWT);
        } catch (JWTVerificationException e) {
            return false; // Token inválido
        }
    }

    // Extrai o email (subject) do token
    public String extractUsername(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            return null; // Token inválido
        }
    }

    // Extrai a role do token
    public String extractRole(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return decodedJWT.getClaim("role").asString();
        } catch (JWTVerificationException e) {
            return null; // Token inválido
        }
    }

    // Verifica se o token expirou
    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }
}