package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Equipe;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.model.Projeto.StatusProjeto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoCreateDTO {
    private String nome;
    private String descricao;
    private String status;
    private Long equipeId; // ID da equipe à qual o projeto pertence

    // Método para converter de ProjetoCreateDTO para Projeto
    public Projeto toProjeto(Equipe equipe, Usuario usuario) {
        Projeto projeto = new Projeto();
        projeto.setNome(this.nome);
        projeto.setDescricao(this.descricao);
        projeto.setStatus(StatusProjeto.valueOf(this.status)); // Converte String para enum
        projeto.setEquipe(equipe);
        projeto.setUsuario(usuario); // O criador do projeto
        return projeto;
    }
}
