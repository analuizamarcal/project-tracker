package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.TarefaDTO;
import com.ana_luiza.project_tracker.mapper.TarefaMapper;
import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Tarefa;
import com.ana_luiza.project_tracker.repository.ProjetoRepository;
import com.ana_luiza.project_tracker.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;
    private final ProjetoService projetoService;

    // Criar uma nova tarefa em um projeto específico
    public TarefaDTO criarTarefa(Long projetoId, TarefaDTO tarefaDTO) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        validarPermissao(projeto);

        Tarefa tarefa = TarefaMapper.toEntity(tarefaDTO, projeto);
        tarefa.setProjeto(projeto);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return TarefaMapper.toDTO(tarefaSalva);
    }

    // Listar todas as tarefas de um projeto
    public List<TarefaDTO> listarTarefas(Long projetoId) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        validarPermissao(projeto);

        return tarefaRepository.findByProjetoId(projetoId)
                .stream()
                .map(TarefaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar uma tarefa específica dentro de um projeto
    public TarefaDTO buscarTarefaPorId(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        validarPermissao(tarefa.getProjeto());
        return TarefaMapper.toDTO(tarefa);
    }
    
    // Buscar ID do projeto através da tarefa
    public Long buscarProjetoIdPorTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));
        return tarefa.getProjeto().getId();
    }

    // Atualizar uma tarefa
    public TarefaDTO atualizarTarefa(Long tarefaId, TarefaDTO tarefaDTO) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        validarPermissao(tarefa.getProjeto());

        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setStatus(tarefaDTO.getStatus());
        tarefa.setDataLimite(tarefaDTO.getDataLimite());

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        return TarefaMapper.toDTO(tarefaAtualizada);
    }

    // Excluir uma tarefa
    public void excluirTarefa(Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        validarPermissao(tarefa.getProjeto());
        tarefaRepository.delete(tarefa);
    }

    // Valida se o usuário autenticado tem permissão para acessar o projeto
    private void validarPermissao(Projeto projeto) {
        if (!projetoService.usuarioTemAcesso(projeto.getId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar este projeto.");
        }
    }
}
