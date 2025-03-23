package com.ana_luiza.project_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projetos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Dono do projeto (ainda pode existir)

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe; // Agora o projeto pertence a uma equipe

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Tarefa> tarefas;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = new Date();
    }

    public enum StatusProjeto {
        PLANEJADO,
        EM_ANDAMENTO,
        CONCLUIDO
    }
}
