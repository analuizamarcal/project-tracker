package com.ana_luiza.project_tracker.mapper;

import com.ana_luiza.project_tracker.dto.ProjetoDTO;
import com.ana_luiza.project_tracker.model.Projeto;

public class ProjetoMapper {

    public static ProjetoDTO toDTO(Projeto projeto) {
        return ProjetoDTO.builder()
                .id(projeto.getId())
                .nome(projeto.getNome())
                .descricao(projeto.getDescricao())
                .status(projeto.getStatus())
                .dataCriacao(projeto.getDataCriacao())
                .usuarioId(projeto.getUsuario().getId()) // Apenas ID do usuário
                .usuarioNome(projeto.getUsuario().getNome()) // Nome do usuário
                .build();
    }
    
    public static Projeto toEntity(ProjetoDTO projetoDTO) {
        Projeto projeto = new Projeto();
        projeto.setId(projetoDTO.getId());
        projeto.setNome(projetoDTO.getNome());
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setStatus(projetoDTO.getStatus());
        projeto.setDataCriacao(projetoDTO.getDataCriacao());
        return projeto;
    }
}
