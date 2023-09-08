package com.lima.projectapi.payload;

import com.lima.projectapi.entity.Activity;

import java.time.LocalDateTime;

public record ActivityPayload(Integer id,
                              String nome,
                              String obs,
                              boolean concluido,
                              LocalDateTime dataInclusao,
                              LocalDateTime dataAlteracao) {

    public static ActivityPayload parse(final Activity activity) {
        return new ActivityPayload(activity.getId(),
                activity.getNome(),
                activity.getObs(),
                activity.isConcluido(),
                activity.getDataInclusao(),
                activity.getDataAlteracao());
    }
}
