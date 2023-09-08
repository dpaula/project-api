package com.lima.projectapi.entity;

import com.lima.projectapi.enuns.EnTipoStatus;
import com.lima.projectapi.payload.ProjectRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Fernando de Lima on 08/09/23
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pro_projetos")
public class Project {

    @Id
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    private Integer id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnTipoStatus status;

    @Column(name = "data_inclusao", nullable = false)
    private LocalDateTime dataInclusao;

    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    @Fetch(value = FetchMode.JOIN)
    private Set<Activity> atividades = new HashSet<>();

    private Integer idCliente;

    public static Project parse(final ProjectRequest projectRequest) {
        return Project.builder()
                .id(projectRequest.id())
                .nome(projectRequest.nome())
                .descricao(projectRequest.descricao())
                .status(projectRequest.status())
                .idCliente(projectRequest.idCliente())
                .build();
    }

    public void incluirAtividade(final Activity activity) {
        atividades.add(activity);
    }
}
