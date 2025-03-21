package com.ana_luiza.project_tracker.service;

import com.ana_luiza.project_tracker.dto.ProjetoDTO;
import com.ana_luiza.project_tracker.mapper.ProjetoMapper;
import com.ana_luiza.project_tracker.model.Projeto;
import com.ana_luiza.project_tracker.model.Usuario;
import com.ana_luiza.project_tracker.repository.ProjetoRepository;
import com.ana_luiza.project_tracker.repository.UsuarioRepository;
import com.ana_luiza.project_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    // Criar um projeto associado ao usuário autenticado
    public ProjetoDTO criarProjeto(ProjetoDTO projetoDTO) {
        Long usuarioId = jwtService.getAuthenticatedUserId();
        if (usuarioId == null) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Projeto projeto = ProjetoMapper.toEntity(projetoDTO);
        projeto.setUsuario(usuario);

        Projeto projetoSalvo = projetoRepository.save(projeto);
        return ProjetoMapper.toDTO(projetoSalvo);
    }

    // Listar todos os projetos do usuário autenticado
    public List<ProjetoDTO> listarProjetos() {
        Long usuarioId = jwtService.getAuthenticatedUserId();
        if (usuarioId == null) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        return projetoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ProjetoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar um projeto específico, garantindo que pertence ao usuário autenticado
    public ProjetoDTO buscarProjetoPorId(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        validarPermissao(projeto);
        return ProjetoMapper.toDTO(projeto);
    }

    // Atualizar um projeto, garantindo que pertence ao usuário autenticado
    public ProjetoDTO atualizarProjeto(Long id, ProjetoDTO projetoDTO) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        validarPermissao(projeto);

        projeto.setNome(projetoDTO.getNome());
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setStatus(projetoDTO.getStatus());

        Projeto projetoAtualizado = projetoRepository.save(projeto);
        return ProjetoMapper.toDTO(projetoAtualizado);
    }

    // Excluir um projeto, garantindo que pertence ao usuário autenticado
    public void excluirProjeto(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        validarPermissao(projeto);
        projetoRepository.delete(projeto);
    }

    // Verifica se o usuário autenticado tem permissão para acessar o projeto
    private void validarPermissao(Projeto projeto) {
        Long usuarioId = jwtService.getAuthenticatedUserId();
        if (!projeto.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para acessar este projeto.");
        }
    }
    
    // Método que verifica se o usuário autenticado tem acesso ao projeto
    public boolean usuarioTemAcesso(Long projetoId) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        // Obtém o ID do usuário autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o usuário é o dono do projeto
        return projeto.getUsuario().getId().equals(usuario.getId());
    }
}
