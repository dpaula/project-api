package com.lima.projectapi.controller.imp;

import com.lima.projectapi.controller.IProjectController;
import com.lima.projectapi.entity.Project;
import com.lima.projectapi.payload.ProjectRequest;
import com.lima.projectapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Fernando de Lima on 08/09/23
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectController implements IProjectController {

    private final ProjectService service;

    @Override
    public ResponseEntity<Page<ProjectRequest>> listar(final String nome, final Pageable pageable) {
        final Page<ProjectRequest> projetosDTO = service
                .findAll(nome, pageable)
                .map(ProjectRequest::parse);

        return ResponseEntity.ok(projetosDTO);
    }

    @Override
    public ResponseEntity<ProjectRequest> postNewActivity(final Integer projectId, final ActivityInput activityInput) {
        final var project = service.incluirAtividade(projectId, activityInput.nome(), activityInput.obs());

        return ResponseEntity.ok(ProjectRequest.parse(project));
    }

    @Override
    public ResponseEntity<ProjectRequest> postFinalizarAtividade(final Integer projectId, final Integer atividadeId) {
        final var project = service.concluirAtividade(projectId, atividadeId);

        return ResponseEntity.ok(ProjectRequest.parse(project));
    }

    @Override
    public ResponseEntity<ProjectRequest> postAlterarAtividade(final Integer projectId, final Integer atividadeId, final ActivityInput activityInput) {
        final var project = service.alterarAtividade(projectId, atividadeId, activityInput.nome(), activityInput.obs());

        return ResponseEntity.ok(ProjectRequest.parse(project));
    }

    @Override
    public ResponseEntity<ProjectRequest> post(final ProjectRequest customerRequest, final UriComponentsBuilder builder) {
        final var projeto = Project.parse(customerRequest);
        final var projetoCriado = ProjectRequest.parse(service.create(projeto));

        final var location = builder.replacePath("/v1/projetos/{id}")
                .buildAndExpand(projetoCriado.id().toString())
                .toUri();

        return ResponseEntity.created(location)
                .body(projetoCriado);
    }

    @Override
    public ResponseEntity<ProjectRequest> put(final ProjectRequest projectRequest) {
        final var project = Project.parse(projectRequest);
        return ResponseEntity.ok(ProjectRequest.parse(service.alterar(project)));
    }

    @Override
    public ResponseEntity<ProjectRequest> get(final Integer id) {
        return ResponseEntity.ok(ProjectRequest.parse(service.buscar(id)));
    }

    @Override
    public void delete(final Integer id) {
        service.delete(id);
    }
}
