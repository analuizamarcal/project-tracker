package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Tarefa.StatusTarefa;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TarefaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private Date dataCriacao;
    private Date dataLimite;
    private Long projetoId;
}
