package com.lima.projectapi.service;

import com.lima.projectapi.entity.Activity;
import com.lima.projectapi.repository.ActivityRepository;
import com.lima.projectapi.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityService service;

    @Mock
    private ActivityRepository repository;

    @Test
    void createAtividadeSucess() {

        final var activitySet = Activity.builder()
                .nome("Atividade 1")
                .obs("Observação 1")
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        service.create(activitySet.getNome(), activitySet.getObs());

        final ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(Activity.class);

        verify(repository, times(1)).saveAndFlush(captor.capture());
    }

    @Test
    void createAtividadeNomeNull() {

        final var activitySet = Activity.builder()
                .obs("Observação 1")
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        assertThatThrownBy(() -> service.create(activitySet.getNome(), activitySet.getObs()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome não pode ser nulo ou vazio");

        verify(repository, never()).saveAndFlush(activitySet);
    }

    @Test
    void concluirAtividade() {

        final var activitySet = Activity.builder()
                .id(1)
                .nome("Atividade 1")
                .obs("Observação 1")
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        when(repository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(activitySet));

        service.concluirAtividade(activitySet.getId());

        verify(repository, times(1)).save(activitySet);
    }

    @Test
    void alterar() {

        final var activitySet = Activity.builder()
                .id(1)
                .nome("Atividade 1")
                .obs("Observação 1")
                .dataInclusao(Util.getDataAtualDateTime())
                .build();

        when(repository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(activitySet));

        service.alterar(activitySet.getId(), activitySet.getNome(), activitySet.getObs());

        final ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);

        verify(repository, times(1)).save(activityArgumentCaptor.capture());
    }
}