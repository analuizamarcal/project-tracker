package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.ProjetoCreateDTO;
import com.ana_luiza.project_tracker.dto.ProjetoViewDTO;
import com.ana_luiza.project_tracker.model.Equipe;
import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.model.Projeto.StatusProjeto;
import com.ana_luiza.project_tracker.repository.EquipeRepository;
import com.ana_luiza.project_tracker.repository.ProjetoRepository;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    // Busca todos os projetos
    public List<ProjetoViewDTO> findAll() {
        return projetoRepository.findAll().stream()
                .map(ProjetoViewDTO::fromProjeto)
                .collect(Collectors.toList());
    }

    // Busca um projeto por ID
    public ProjetoViewDTO findById(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        return ProjetoViewDTO.fromProjeto(projeto);
    }
    
    // Busca projetos por ID da equipe
    public List<ProjetoViewDTO> findByEquipeId(Long equipeId) {
        return projetoRepository.findByEquipeId(equipeId).stream()
                .map(ProjetoViewDTO::fromProjeto)
                .collect(Collectors.toList());
    }
    
    // Busca projetos por ID do usuário
    public List<ProjetoViewDTO> findByUsuarioId(Long usuarioId) {
        return projetoRepository.findByUsuarioId(usuarioId).stream()
                .map(ProjetoViewDTO::fromProjeto)
                .collect(Collectors.toList());
    }

    // Cria um novo projeto
    public ProjetoViewDTO create(ProjetoCreateDTO projetoCreateDTO) {
        Usuario usuario = getAuthenticatedUser();
        Equipe equipe = equipeRepository.findById(projetoCreateDTO.getEquipeId())
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));

        Projeto projeto = projetoCreateDTO.toProjeto(equipe, usuario);
        projeto = projetoRepository.save(projeto);
        return ProjetoViewDTO.fromProjeto(projeto);
    }

    // Atualiza um projeto existente
    public ProjetoViewDTO update(Long id, ProjetoCreateDTO projetoCreateDTO) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        projeto.setNome(projetoCreateDTO.getNome());
        projeto.setDescricao(projetoCreateDTO.getDescricao());
        projeto.setStatus(StatusProjeto.valueOf(projetoCreateDTO.getStatus())); // Corrigido

        projeto = projetoRepository.save(projeto);
        return ProjetoViewDTO.fromProjeto(projeto);
    }

    // Deleta um projeto
    public void delete(Long id) {
        projetoRepository.deleteById(id);
    }

    // Obtém o usuário autenticado
    private Usuario getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
        }
        throw new RuntimeException("Usuário não autenticado");
    }
}