package com.ana_luiza.project_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
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
    private StatusTarefa status = StatusTarefa.PENDENTE;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();

    @Temporal(TemporalType.DATE)
    private Date dataLimite;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
    
    public enum StatusTarefa {
        PENDENTE,
        EM_PROGRESSO,
        CONCLUIDA
    }
}
