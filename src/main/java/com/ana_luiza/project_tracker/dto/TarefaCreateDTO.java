package com.ana_luiza.project_tracker.dto;

import lombok.*;
import com.ana_luiza.project_tracker.model.Tarefa;
import com.ana_luiza.project_tracker.model.Tarefa.StatusTarefa;
import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Equipe;
import com.ana_luiza.project_tracker.model.Usuario;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaCreateDTO {
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private Date dataLimite;
    private Long projetoId;
    private Long equipeId;
    private Long responsavelId;

    public Tarefa toTarefa(Projeto projeto, Equipe equipe, Usuario responsavel) {
        return Tarefa.builder()
                .titulo(this.titulo)
                .descricao(this.descricao)
                .status(this.status != null ? this.status : StatusTarefa.PENDENTE)
                .dataLimite(this.dataLimite)
                .projeto(projeto)
                .equipe(equipe)
                .responsavel(responsavel)
                .build();
    }
}
