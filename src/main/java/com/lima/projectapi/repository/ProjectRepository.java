package com.lima.projectapi.repository;

import com.lima.projectapi.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Fernando de Lima on 08/09/23
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    boolean existsByNome(String nome);

    Page<Project> findAllByNome(String nome, Pageable pageable);
}
