package com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InscripcionProyectoDTO {
    private Long proyectoId;
    private Long convocatoriaId;
    private String estado;
}
