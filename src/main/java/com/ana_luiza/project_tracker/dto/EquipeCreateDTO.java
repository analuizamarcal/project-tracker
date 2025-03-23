package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Equipe;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipeCreateDTO {
    private String nome;

    // Método para converter de EquipeCreateDTO para Equipe
    public Equipe toEquipe() {
        Equipe equipe = new Equipe();
        equipe.setNome(this.nome);
        return equipe;
    }
}
