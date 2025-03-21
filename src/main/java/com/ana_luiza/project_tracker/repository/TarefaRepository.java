package com.ana_luiza.project_tracker.repository;

import com.ana_luiza.project_tracker.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByProjetoId(Long projetoId);
}
