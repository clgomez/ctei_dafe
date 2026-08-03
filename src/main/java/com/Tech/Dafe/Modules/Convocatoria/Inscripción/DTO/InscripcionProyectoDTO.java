package com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InscripcionProyectoDTO {
    private Long proyectoId;
    private Long convocatoriaId;

    @NotBlank(message = "El estado de la inscripción es obligatoria")
    @Pattern(
        regexp = "^$|^(PENDIENTE|APROBADO|CANCELADO|FINALIZADO)$",
        message = "El estado de la inscripción solo puede ser PENDIENTE, APROBADO, CANCELADO y FINALIZADO"
    )
    private String estado;
}
