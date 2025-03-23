package com.ana_luiza.project_tracker.repository;

import com.ana_luiza.project_tracker.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
}
