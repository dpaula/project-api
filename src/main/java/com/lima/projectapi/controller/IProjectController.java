package com.lima.projectapi.controller;

import com.lima.projectapi.payload.ProjectRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Fernando de Lima on 08/09/23
 */
@RestController
@RequestMapping("/v1/projetos")
@CrossOrigin(origins = "*")
@Tag(name = "Projetos", description = "Serviços para gerenciamento dos Projetos")
public interface IProjectController {

    record ActivityInput(String nome, String obs) {
    }

    @Operation(summary = "Buscar Projeto(s)", description = "Get para buscar um ou vários Projetos")
    @GetMapping()
    ResponseEntity<Page<ProjectRequest>> listar(
            @RequestParam(value = "nome", required = false) String nome,
            @PageableDefault(sort = "dataInclusao", direction = Sort.Direction.DESC, size = 20) @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Alterar Projeto", description = "Put para alterar um projeto")
    @Transactional
    @PutMapping
    ResponseEntity<ProjectRequest> put(@NotNull @Valid @RequestBody ProjectRequest customerRequest);

    @Operation(summary = "Buscar Projeto", description = "Get para buscar um Projeto pelo Id")
    @GetMapping(value = "/{id}")
    ResponseEntity<ProjectRequest> get(@PathVariable Integer id);

    @Operation(summary = "Remover Projeto", description = "Delete para remover um Projeto pelo Id")
    @DeleteMapping(value = "/{id}")
    @Transactional
    void delete(@PathVariable Integer id);

    @Operation(summary = "Incluir Projeto", description = "Post para incluir novo Projeto")
    @Transactional
    @PostMapping
    ResponseEntity<ProjectRequest> post(@NotNull @Valid @RequestBody ProjectRequest customerRequest,
                                        UriComponentsBuilder builder);

    @Operation(summary = "Incluir Atividade ao Projeto", description = "Post para incluir uma nova Atividade ao Projeto")
    @Transactional
    @PostMapping(value = "/{projectId}/atividade")
    ResponseEntity<ProjectRequest> postNewActivity(@PathVariable Integer projectId, @RequestBody ActivityInput activityInput);

    @Operation(summary = "Finalizar Atividade", description = "Post para concluir Atividade do Projeto")
    @Transactional
    @PostMapping(value = "/{projectId}/atividade/{atividadeId}/finalizar")
    ResponseEntity<ProjectRequest> postFinalizarAtividade(@PathVariable Integer projectId, @PathVariable Integer atividadeId);

    @Operation(summary = "Alterar Atividade", description = "Post para alterar Atividade do Projeto")
    @Transactional
    @PostMapping(value = "/{projectId}/atividade/{atividadeId}")
    ResponseEntity<ProjectRequest> postAlterarAtividade(@PathVariable Integer projectId,
                                                        @PathVariable Integer atividadeId,
                                                        @RequestBody ActivityInput activityInput);
}
