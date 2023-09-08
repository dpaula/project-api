package com.lima.projectapi.repository;

import com.lima.projectapi.entity.Project;
import com.lima.projectapi.enuns.EnTipoStatus;
import com.lima.projectapi.util.Util;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void existsByNome() {

        final var project = getProject();

        projectRepository.save(project);

        final var existsByNome = projectRepository.existsByNome("Projeto 1");
        assertThat(existsByNome).isTrue();
    }

    @Test
    void findAllByNome() {

        final var project = getProject();

        projectRepository.save(project);

        final var allByNome = projectRepository.findAllByNome("Projeto 1", Mockito.any(Pageable.class));

        assertThat(allByNome).isNotNull();
        assertThat(allByNome).isInstanceOf(Page.class);

        assertThat(allByNome.getTotalElements()).isEqualTo(1);
        assertThat(allByNome.getTotalPages()).isEqualTo(1);
        assertThat(allByNome.getContent().get(0).getNome()).isEqualTo("Projeto 1");
        assertThat(allByNome.getContent().get(0).getStatus()).isEqualTo(EnTipoStatus.NOVO);
        assertThat(allByNome.getContent().get(0).getDataInclusao()).isNotNull();
        assertThat(allByNome.getContent().get(0).getDataAlteracao()).isNull();
        assertThat(allByNome.getContent().get(0).getAtividades().size()).isEqualTo(0);
    }

    private static Project getProject() {
        return Project.builder()
                .nome("Projeto 1")
                .status(EnTipoStatus.NOVO)
                .dataInclusao(Util.getDataAtualDateTime())
                .build();
    }
}