package com.Tech.Dafe.Modules.Calificaciones.DTO;

import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.RolCalificador;

import lombok.Data;

@Data
public class EvaluacionDetalleDTO {

    private Long id;

    private Long calificacionId;

    private Long rubroId;

    private Long usuarioId;

    private RolCalificador rolCalificador;

    private Float nota;

    private String comentario;

    private Boolean activo;

    private Long idUsuarioHistorico;

    private String nombreUsuarioHistorico;

}