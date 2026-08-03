package com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDTO {

    private Long id;

    private LocalDateTime fechaInscripcion;
    private LocalDateTime fechaActualizacion;
    private String estado;
    private Long usuarioId;
    private Long proyectoId;
    private Long convocatoriaId;
    
    
    
}
