package com.Tech.Dafe.Modules.Calificaciones.Models;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.RolCalificador;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "evaluacion_detalle")
public class EvaluacionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rubro_id")
    private RubroCalificacion rubro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private RolCalificador rolCalificador;

    private Float nota;

    @Column(length = 1000)
    private String comentario;

    private Boolean activo = true;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaEvaluacion;

    private Long idUsuarioHistorico;

    private String nombreUsuarioHistorico;

    @PrePersist
    public void prePersist() {
        fechaEvaluacion = LocalDateTime.now();
    }
}