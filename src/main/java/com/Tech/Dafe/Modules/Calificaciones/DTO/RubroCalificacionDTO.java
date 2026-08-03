package com.Tech.Dafe.Modules.Calificaciones.DTO;

import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.TipoCalificacion;
import java.util.List;
import lombok.Data;

@Data
public class RubroCalificacionDTO {

    private Long id;

    private TipoCalificacion tipoCalificacion;

    private Long referenciaId;

    private String descripcion;

    private List<EvaluacionDetalleDTO> evaluaciones;
}