package com.ana_luiza.project_tracker.dto;

import com.ana_luiza.project_tracker.model.Equipe;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipeViewDTO {
    private Long id;
    private String nome;
    private String administradorNome;
    private List<String> usuarios; // Lista com os nomes dos usuários da equipe

    // Método para converter de Equipe para EquipeViewDTO
    public static EquipeViewDTO fromEquipe(Equipe equipe) {
        return new EquipeViewDTO(
            equipe.getId(),
            equipe.getNome(),
            equipe.getAdministrador().getNome(),
            equipe.getUsuarios().stream().map(u -> u.getNome()).collect(Collectors.toList())
        );
    }
}
