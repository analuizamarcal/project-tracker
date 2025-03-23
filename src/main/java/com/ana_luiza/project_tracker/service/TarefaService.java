package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.TarefaCreateDTO;
import com.ana_luiza.project_tracker.dto.TarefaViewDTO;
import com.ana_luiza.project_tracker.model.Tarefa;
import com.ana_luiza.project_tracker.repository.EquipeRepository;
import com.ana_luiza.project_tracker.repository.ProjetoRepository;
import com.ana_luiza.project_tracker.repository.TarefaRepository;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipeRepository equipeRepository;
    
    @Autowired 
    private ProjetoRepository projetoRepository;
    
    // Busca todas as tarefas
    public List<TarefaViewDTO> findAll() {
        return tarefaRepository.findAll().stream()
                .map(TarefaViewDTO::fromTarefa)
                .collect(Collectors.toList());
    }

    // Busca uma tarefa por ID
    public TarefaViewDTO findById(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        return TarefaViewDTO.fromTarefa(tarefa);
    }

    // Busca tarefas por ID do projeto
    public List<TarefaViewDTO> findByProjetoId(Long projetoId) {
        return tarefaRepository.findByProjetoId(projetoId).stream()
                .map(TarefaViewDTO::fromTarefa)
                .collect(Collectors.toList());
    }

    // Busca tarefas por ID do responsável (usuário)
    public List<TarefaViewDTO> findByResponsavelId(Long responsavelId) {
        return tarefaRepository.findByResponsavelId(responsavelId).stream()
                .map(TarefaViewDTO::fromTarefa)
                .collect(Collectors.toList());
    }

    // Cria uma nova tarefa
    public TarefaViewDTO create(TarefaCreateDTO tarefaDTO) {
        Tarefa tarefa = tarefaDTO.toTarefa(
                projetoRepository.findById(tarefaDTO.getProjetoId())
                        .orElseThrow(() -> new RuntimeException("Projeto não encontrado")),
                equipeRepository.findById(tarefaDTO.getEquipeId())
                        .orElseThrow(() -> new RuntimeException("Equipe não encontrada")),
                usuarioRepository.findById(tarefaDTO.getResponsavelId())
                        .orElseThrow(() -> new RuntimeException("Responsável não encontrado"))
        );
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        return TarefaViewDTO.fromTarefa(novaTarefa);
    }

    // Atualiza uma tarefa existente
    public TarefaViewDTO update(Long id, TarefaCreateDTO tarefaDTO) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setStatus(tarefaDTO.getStatus());
        tarefa.setDataLimite(tarefaDTO.getDataLimite());
        tarefa.setResponsavel(usuarioRepository.findById(tarefaDTO.getResponsavelId())
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado")));

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        return TarefaViewDTO.fromTarefa(tarefaAtualizada);
    }

    // Deleta uma tarefa
    public void delete(Long id) {
        tarefaRepository.deleteById(id);
    }
}
