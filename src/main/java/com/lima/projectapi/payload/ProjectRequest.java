package com.lima.projectapi.payload;

import com.lima.projectapi.entity.Project;
import com.lima.projectapi.enuns.EnTipoStatus;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectRequest(Integer id,
                             @NotEmpty String nome,
                             String descricao,
                             EnTipoStatus status,
                             LocalDateTime dataInclusao,
                             LocalDateTime dataAlteracao,
                             List<ActivityPayload> atividades,
                             Integer idCliente) {

    public static ProjectRequest parse(final Project project) {
        return new ProjectRequest(project.getId(),
                project.getNome(),
                project.getDescricao(),
                project.getStatus(),
                project.getDataInclusao(),
                project.getDataAlteracao(),
                project.getAtividades().stream().map(ActivityPayload::parse).toList(),
                project.getIdCliente());
    }
}
