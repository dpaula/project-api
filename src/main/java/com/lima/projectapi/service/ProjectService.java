package com.lima.projectapi.service;

import com.lima.projectapi.client.customer.CustomerClient;
import com.lima.projectapi.client.customer.CustomerResponse;
import com.lima.projectapi.entity.Project;
import com.lima.projectapi.enuns.EnTipoStatus;
import com.lima.projectapi.error.ConflictException;
import com.lima.projectapi.error.ObjectNotFoundException;
import com.lima.projectapi.repository.ProjectRepository;
import com.lima.projectapi.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author Fernando de Lima on 08/09/23
 */
@Slf4j
@Service
public record ProjectService(ProjectRepository repository,
                             ActivityService activityService,
                             CustomerClient customerClient) {

    private Project findProjectByIdOrThrow(final Integer id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException.with(Project.class, id, "id"));
    }

    private void validarNomeConflito(final String nome) {
        if (repository.existsByNome(nome)) {
            throw new ConflictException("Já existe Projeto cadastrado para mesmo nome: " + nome);
        }
    }

    public Project buscar(final Integer id) {
        log.info("Buscando project id: {}", id);
        return findProjectByIdOrThrow(id);
    }

    public void delete(final Integer id) {
        log.info("Removendo project id: {}", id);

        final var project = findProjectByIdOrThrow(id);
        repository.delete(project);
    }

    public Page<Project> findAll(final String nome, final Pageable pageable) {

        if (isNull(nome)) {
            log.info("Buscando todos os projects");
            return repository.findAll(pageable);
        }

        log.info("Buscando projects para o filtro {}", nome);

        return repository.findAllByNome(nome, pageable);
    }

    public Project create(final Project projeto) {
        log.info("Criando novo projeto: {}", projeto.getNome());

        validarNomeConflito(projeto.getNome());

        validarClienteProjeto(projeto);

        projeto.setDataInclusao(Util.getDataAtualDateTime());
        projeto.setStatus(EnTipoStatus.NOVO);

        return repository.saveAndFlush(projeto);
    }

    private void validarClienteProjeto(final Project projeto) {
        final var customer = customerClient.getCustomer(projeto.getIdCliente());

        if (isNull(customer)) {
            throw new ObjectNotFoundException(CustomerResponse.class, projeto.getIdCliente(), "id");
        }

        if (customer.ativo()) {
            projeto.setIdCliente(customer.id());
            log.info("Cliente {} vinculado ao projeto {}", customer.id(), projeto.getNome());
            return;
        }

        throw new ConflictException("Cliente inativo, id: " + projeto.getIdCliente() + " nome: " + customer.nome());
    }

    public Project alterar(final Project projeto) {
        log.info("Alterando projeto id: {}", projeto.getId());

        final var projetoBase = findProjectByIdOrThrow(projeto.getId());

        if (!projeto.getNome().equalsIgnoreCase(projetoBase.getNome())) {
            validarNomeConflito(projeto.getNome());
            projetoBase.setNome(projetoBase.getNome());
        }

        projetoBase.setNome(projeto.getNome());
        projetoBase.setDescricao(projeto.getDescricao());
        projetoBase.setStatus(projeto.getStatus());
        projetoBase.setDataAlteracao(Util.getDataAtualDateTime());

        return repository.save(projetoBase);
    }

    public Project incluirAtividade(final Integer projectId, final String nomeAtividade, final String obsAtividade) {
        log.info("Incluindo atividade no projeto id: {}", projectId);

        final var projeto = findProjectByIdOrThrow(projectId);

        final var activity = activityService.create(nomeAtividade, obsAtividade);

        projeto.incluirAtividade(activity);

        projeto.setDataAlteracao(Util.getDataAtualDateTime());

        return repository.save(projeto);
    }

    public Project concluirAtividade(final Integer projectId, final Integer atividadeId) {
        log.info("Concluindo atividade id: {}", atividadeId);

        activityService.concluirAtividade(atividadeId);

        return findProjectByIdOrThrow(projectId);
    }

    public Project alterarAtividade(final Integer projectId, final Integer atividadeId, final String nome, final String obs) {
        log.info("Alterando atividade id: {}", atividadeId);

        activityService.alterar(atividadeId, nome, obs);

        return findProjectByIdOrThrow(projectId);
    }
}
