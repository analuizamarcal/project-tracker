package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Projeto.StatusProjeto;
import lombok.*;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetoDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private StatusProjeto status;
    private Date dataCriacao;
    
    private Long usuarioId; // Exibir apenas o ID do usuário
    private String usuarioNome; // Exibir apenas o nome do usuário
}
