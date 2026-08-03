package com.Tech.Dafe.Modules.Calificaciones.Models;

import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.TipoCalificacion;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "rubro_calificacion")
public class RubroCalificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCalificacion tipoCalificacion;

    private Long referenciaId;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "calificacion_id")
    private Calificacion calificacion;

    @OneToMany(
            mappedBy = "rubro",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    
    private List<EvaluacionDetalle> evaluaciones = new ArrayList<>();
}