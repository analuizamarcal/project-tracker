package com.ana_luiza.project_tracker.mapper;

import java.util.Date;

import com.ana_luiza.project_tracker.dto.TarefaDTO;
import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Tarefa;

public class TarefaMapper {

    public static TarefaDTO toDTO(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setStatus(tarefa.getStatus());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setDataLimite(tarefa.getDataLimite());
        dto.setProjetoId(tarefa.getProjeto().getId());
        return dto;
    }

    public static Tarefa toEntity(TarefaDTO dto, Projeto projeto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setStatus(dto.getStatus());
        tarefa.setDataCriacao(dto.getDataCriacao() != null ? dto.getDataCriacao() : new Date());
        tarefa.setDataLimite(dto.getDataLimite());
        tarefa.setProjeto(projeto);
        return tarefa;
    }
}
