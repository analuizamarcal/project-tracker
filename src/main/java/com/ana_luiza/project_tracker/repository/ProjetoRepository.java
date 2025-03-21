package com.ana_luiza.project_tracker.repository;

import com.ana_luiza.project_tracker.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByUsuarioId(Long usuarioId);
}
