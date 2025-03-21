package com.ana_luiza.project_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	public enum Role {
		ADMIN, // Gerencia usuários, projetos e acessa todos os relatórios.
		CONSULTOR, // Acompanha projetos, gera insights e sugere melhorias com base nos dados
		CLIENTE // Visualiza apenas os projetos e relatórios da sua empresa
	}
}
