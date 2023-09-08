package com.lima.projectapi.service;

import com.lima.projectapi.client.customer.CustomerClient;
import com.lima.projectapi.client.customer.CustomerResponse;
import com.lima.projectapi.entity.Activity;
import com.lima.projectapi.entity.Project;
import com.lima.projectapi.enuns.EnTipoStatus;
import com.lima.projectapi.error.ConflictException;
import com.lima.projectapi.error.ObjectNotFoundException;
import com.lima.projectapi.repository.ProjectRepository;
import com.lima.projectapi.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService service;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityService activityService;

    @Mock
    private CustomerClient customerClient;

    @Test
    void createProjectSucesso() {

        final var project = getProject();

        final var customerResponse = new CustomerResponse(1, "Cliente 1", "cliente1@gmail.com", true);

        when(customerClient.getCustomer(anyInt()))
                .thenReturn(customerResponse);

        service.create(project);

        verify(projectRepository, times(1)).saveAndFlush(project);
    }

    @Test
    void createProjectClienteNull() {

        final var project = getProject();

        when(customerClient.getCustomer(anyInt()))
                .thenReturn(any(CustomerResponse.class));

        assertThatThrownBy(() -> service.create(project))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Objeto não encontrado: <id>: <1> Class: <com.lima.projectapi.client.customer.CustomerResponse>");

        verify(projectRepository, never()).saveAndFlush(project);
    }

    @Test
    void createProjectClienteConflito() {

        final var project = getProject();

        final var customerResponse = new CustomerResponse(1, "Cliente 1", "cliente1@gmail.com", false);

        when(customerClient.getCustomer(anyInt()))
                .thenReturn(customerResponse);

        assertThatThrownBy(() -> service.create(project))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Cliente inativo, id: 1 nome: Cliente 1");

        verify(projectRepository, never()).saveAndFlush(project);
    }

    @Test
    void deveBuscarProjetoComSucesso() {

        final var project = getProjectId();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        service.buscar(project.getId());

        verify(projectRepository, times(1)).findById(project.getId());
    }

    @Test
    void delete() {

        final var project = getProjectId();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        service.delete(project.getId());

        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    void findAll() {

        final var any = Mockito.any(Pageable.class);

        service.findAll(null, any);

        verify(projectRepository, times(1)).findAll(any);
    }

    @Test
    void findAllByName() {

        final var project = getProjectId();

        final Page<Project> projects = Mockito.mock(Page.class);

        when(projectRepository.findAllByNome(Mockito.anyString(), Mockito.any()))
                .thenReturn(projects);

        service.findAll(project.getNome(), null);

        verify(projectRepository, times(1)).findAllByNome(project.getNome(), null);
    }

    @Test
    void alterarSucesso() {

        final var project = getProjectId();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        service.alterar(project);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void alterarComConflitoNome() {

        final var project = getProjectId();

        final var projectSet = getProjectSet();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        service.alterar(projectSet);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void concluirAtividade() {

        final var project = getProjectId();

        final var projectSet = getProjectSet();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        final var project1 = service.concluirAtividade(projectSet.getId(), 1);

        assertThat(project1).isNotNull();
    }

    @Test
    void alterarAtividade() {

        final var project = getProjectId();

        final var projectSet = getProjectSet();

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        final var project1 = service.alterarAtividade(projectSet.getId(), 1, "Atividade 1", "Obs Atividade 1");

        assertThat(project1).isNotNull();
    }

    @Test
    void incluirAtividade() {

        final var project = getProjectId();

        final Integer projectId = 1;
        final String nomeAtividade = "Atividade 1";
        final String obsAtividade = "Obs Atividade 1";

        final Activity atividade = Activity.builder()
                .id(1)
                .nome(nomeAtividade)
                .obs(obsAtividade)
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        when(activityService.create(nomeAtividade, obsAtividade))
                .thenReturn(atividade);

        when(projectRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(project));

        service.incluirAtividade(projectId, nomeAtividade, obsAtividade);

        verify(projectRepository, times(1)).save(project);
    }

    private static Project getProject() {
        return Project.builder()
                .nome("Projeto 1")
                .status(EnTipoStatus.NOVO)
                .dataInclusao(Util.getDataAtualDateTime())
                .idCliente(1)
                .build();
    }

    private static Project getProjectId() {
        return Project.builder()
                .id(1)
                .nome("Projeto 1")
                .status(EnTipoStatus.NOVO)
                .dataInclusao(Util.getDataAtualDateTime())
                .idCliente(1)
                .build();
    }

    private static Project getProjectSet() {
        return Project.builder()
                .id(1)
                .nome("Projeto 1 Alterado")
                .status(EnTipoStatus.NOVO)
                .idCliente(1)
                .build();
    }
}