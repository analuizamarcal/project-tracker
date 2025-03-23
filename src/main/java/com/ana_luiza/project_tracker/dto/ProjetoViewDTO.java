package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Projeto;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoViewDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String status;
    private Date dataCriacao;
    private String criadorNome;
    private String equipeNome;
    private List<String> tarefas; // Lista de nomes das tarefas associadas ao projeto

    // Método para converter de Projeto para ProjetoViewDTO
    public static ProjetoViewDTO fromProjeto(Projeto projeto) {
        return new ProjetoViewDTO(
            projeto.getId(),
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getStatus().name(),
            projeto.getDataCriacao(),
            projeto.getUsuario().getNome(),
            projeto.getEquipe().getNome(),
            projeto.getTarefas().stream().map(t -> t.getTitulo()).collect(Collectors.toList())
            );
    }
}
