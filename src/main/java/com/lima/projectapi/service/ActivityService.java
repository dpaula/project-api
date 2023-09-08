package com.lima.projectapi.service;

import com.lima.projectapi.entity.Activity;
import com.lima.projectapi.error.ObjectNotFoundException;
import com.lima.projectapi.repository.ActivityRepository;
import com.lima.projectapi.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Fernando de Lima on 08/09/23
 */
@Slf4j
@Service
public record ActivityService(ActivityRepository repository) {

    public Activity create(final String nome, final String obs) {

        if (isBlank(nome)) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        log.info("Criando nova atividade: {}", nome);

        final var atividade = Activity.builder()
                .nome(nome)
                .obs(obs)
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        atividade.setDataInclusao(Util.getDataAtualDateTime());

        return repository.saveAndFlush(atividade);
    }

    public void concluirAtividade(final Integer id) {
        log.info("Concluindo atividade id: {}", id);

        final var atividade = findAtividadeByIdOrThrow(id);

        atividade.setConcluido(true);
        atividade.setDataAlteracao(Util.getDataAtualDateTime());

        repository.save(atividade);
    }

    private Activity findAtividadeByIdOrThrow(final Integer id) {
        return repository.findById(id)
                .orElseThrow(ObjectNotFoundException.with(Activity.class, id, "id"));
    }

    public void alterar(final Integer atividadeId, final String nome, final String obs) {
        log.info("Alterando atividade id: {}", atividadeId);

        final var atividade = findAtividadeByIdOrThrow(atividadeId);

        if (isBlank(nome)) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        atividade.setNome(nome);
        atividade.setObs(obs);
        atividade.setDataAlteracao(Util.getDataAtualDateTime());

        repository.save(atividade);
    }
}
