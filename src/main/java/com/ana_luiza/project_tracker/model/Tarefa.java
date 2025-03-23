package com.ana_luiza.project_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Builder
@Entity
@Table(name = "tarefas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default // Indica que este valor é padrão no builder
    private StatusTarefa status = StatusTarefa.PENDENTE;

    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default // Indica que este valor é padrão no builder
    private Date dataCriacao = new Date();

    @Temporal(TemporalType.DATE)
    private Date dataLimite;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe; // Agora a tarefa pertence a uma equipe

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Usuario responsavel; // O usuário responsável pela tarefa

    public enum StatusTarefa {
        PENDENTE,
        EM_PROGRESSO,
        CONCLUIDA
    }
}
