package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.EquipeCreateDTO;
import com.ana_luiza.project_tracker.dto.EquipeViewDTO;
import com.ana_luiza.project_tracker.model.Equipe;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.EquipeRepository;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Busca todas as equipes
    public List<EquipeViewDTO> findAll() {
        return equipeRepository.findAll().stream()
                .map(EquipeViewDTO::fromEquipe)
                .collect(Collectors.toList());
    }

    // Busca uma equipe por ID
    public EquipeViewDTO findById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));
        return EquipeViewDTO.fromEquipe(equipe);
    }

    // Cria uma nova equipe com o usuário autenticado como administrador
    public EquipeViewDTO create(EquipeCreateDTO equipeDTO) {
        // Obtém o usuário autenticado
        Usuario usuarioLogado = getUsuarioAutenticado();

        // Converte DTO para entidade e define o admin como o usuário autenticado
        Equipe equipe = equipeDTO.toEquipe();
        equipe.setAdministrador(usuarioLogado);

        equipe = equipeRepository.save(equipe);
        return EquipeViewDTO.fromEquipe(equipe);
    }

    // Atualiza uma equipe existente
    public EquipeViewDTO update(Long id, EquipeCreateDTO equipeDTO) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));

        Equipe equipeAtualizada = equipeDTO.toEquipe();
        equipeAtualizada.setId(equipe.getId()); // Mantém o mesmo ID
        equipe = equipeRepository.save(equipeAtualizada);

        return EquipeViewDTO.fromEquipe(equipe);
    }

    // Deleta uma equipe
    public void delete(Long id) {
        equipeRepository.deleteById(id);
    }
    
    // Adiciona membro(s) a uma equipe
    public EquipeViewDTO adicionarMembros(Long equipeId, List<Long> usuariosIds) {
        // Busca a equipe pelo ID
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));

        // Busca os usuários correspondentes aos IDs fornecidos
        List<Usuario> usuarios = usuarioRepository.findAllById(usuariosIds);

        // Adiciona os usuários à equipe e define a equipe para cada usuário
        for (Usuario usuario : usuarios) {
            usuario.setEquipe(equipe); // Define a equipe para o usuário
            equipe.getUsuarios().add(usuario); // Adiciona o usuário à equipe
        }

        // Salva a equipe (os usuários serão salvos em cascata)
        equipe = equipeRepository.save(equipe);

        // Retorna o DTO da equipe atualizada
        return EquipeViewDTO.fromEquipe(equipe);
    }

    // Método auxiliar para obter o usuário autenticado
    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(username) // Supondo que o login seja pelo email
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        } else {
            throw new RuntimeException("Usuário não autenticado");
        }
    }
}
