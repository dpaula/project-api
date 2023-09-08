package com.lima.projectapi.controller.imp;

import com.lima.projectapi.entity.Project;
import com.lima.projectapi.enuns.EnTipoStatus;
import com.lima.projectapi.payload.ProjectRequest;
import com.lima.projectapi.service.ProjectService;
import com.lima.projectapi.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import util.JsonUtilsTest;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProjectController.class)
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    private static final String URI_PROJETOS = "/v1/projetos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    void validarPostProject() throws Exception {

        final var project = getProject();

        Mockito.when(projectService.create(Mockito.any()))
                .thenReturn(project);

        final var projectRequest = new ProjectRequest(null, "Projeto 1", "Descrição 1", null, null, null, null, 1);

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PROJETOS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonUtilsTest.toJson(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Projeto 1"))
                .andExpect(jsonPath("$.descricao").value("Descrição 1"))
                .andExpect(jsonPath("$.status").value("NOVO"))
                .andExpect(jsonPath("$.dataInclusao").isNotEmpty())
                .andExpect(jsonPath("$.dataAlteracao").isEmpty())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.atividades").isEmpty());
    }

    @Test
    void validarListarProjects() throws Exception {

        final var project = getProject();

        final PageImpl<Project> projects = new PageImpl<>(List.of(project));

        Mockito.when(projectService.findAll(Mockito.any(), Mockito.any()))
                .thenReturn(projects);

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PROJETOS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].nome").value("Projeto 1"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição 1"))
                .andExpect(jsonPath("$.content[0].status").value("NOVO"))
                .andExpect(jsonPath("$.content[0].dataInclusao").isNotEmpty())
                .andExpect(jsonPath("$.content[0].dataAlteracao").isEmpty())
                .andExpect(jsonPath("$.content[0].idCliente").value(1))
                .andExpect(jsonPath("$.content[0].atividades").isEmpty());
    }


    private static Project getProject() {
        return Project.builder()
                .id(1)
                .nome("Projeto 1")
                .descricao("Descrição 1")
                .dataInclusao(Util.getDataAtualDateTime())
                .idCliente(1)
                .status(EnTipoStatus.NOVO)
                .atividades(Set.of())
                .build();
    }
}