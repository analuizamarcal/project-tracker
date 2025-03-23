package com.ana_luiza.project_tracker.repository;

import com.ana_luiza.project_tracker.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    List<Tarefa> findByProjetoId(Long projetoId);
    
    List<Tarefa> findByEquipeId(Long equipeId);
    
    List<Tarefa> findByResponsavelId(Long responsavelId);
}
