package com.Tech.Dafe.Modules.Calificaciones.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RubroEvaluacionDTO {

    private String tipo;

    private Long referenciaId;

    private String descripcion;
}