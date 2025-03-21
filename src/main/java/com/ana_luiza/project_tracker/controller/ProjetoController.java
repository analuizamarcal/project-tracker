package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.ProjetoDTO;
import com.ana_luiza.project_tracker.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @Autowired
    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    // Criar um novo projeto (somente usuários autenticados)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public String criarProjeto(@RequestBody ProjetoDTO projetoDTO) {
        projetoService.criarProjeto(projetoDTO);
        return "Projeto criado com sucesso!";
    }

    // Listar todos os projetos do usuário autenticado
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ProjetoDTO> listarProjetos() {
        return projetoService.listarProjetos();
    }

    // Buscar um projeto por ID (somente o dono do projeto ou ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @projetoService.usuarioTemAcesso(#id)")
    public ProjetoDTO buscarProjetoPorId(@PathVariable Long id) {
        return projetoService.buscarProjetoPorId(id);
    }

    // Atualizar um projeto (somente o dono do projeto ou ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("@projetoService.usuarioTemAcesso(#id)")
    public String atualizarProjeto(@PathVariable Long id, @RequestBody ProjetoDTO projetoDTO) {
        projetoService.atualizarProjeto(id, projetoDTO);
        return "Projeto atualizado com sucesso!";
    }

    // Excluir um projeto (somente ADMIN pode excluir)
    @DeleteMapping("/{id}")
    @PreAuthorize("@projetoService.usuarioTemAcesso(#id)")
    public String excluirProjeto(@PathVariable Long id) {
        projetoService.excluirProjeto(id);
        return "Projeto excluído com sucesso!";
    }
}
