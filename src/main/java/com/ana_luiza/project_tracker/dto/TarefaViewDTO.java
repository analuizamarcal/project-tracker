package com.ana_luiza.project_tracker.dto;

import lombok.*;
import com.ana_luiza.project_tracker.model.Tarefa;
import com.ana_luiza.project_tracker.model.Tarefa.StatusTarefa;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaViewDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private Date dataCriacao;
    private Date dataLimite;
    private String projetoNome;
    private String equipeNome;
    private String responsavelNome;

    public static TarefaViewDTO fromTarefa(Tarefa tarefa) {
        return new TarefaViewDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataLimite(),
                tarefa.getProjeto().getNome(),
                tarefa.getEquipe().getNome(),
                tarefa.getResponsavel().getNome()
        );
    }
}
