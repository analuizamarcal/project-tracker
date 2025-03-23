package com.ana_luiza.project_tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "equipes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Usuario administrador; // Somente esse usuário pode gerenciar a equipe
}
