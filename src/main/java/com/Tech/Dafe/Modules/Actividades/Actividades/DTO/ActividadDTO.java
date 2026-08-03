package com.Tech.Dafe.Modules.Actividades.Actividades.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActividadDTO {

    private Long id;

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    @Pattern(
        regexp = "^$|.{10,}$",
        message = "El nombre de la actividad debe tener al menos 10 caracteres"
    )
    private String nombre;

    @NotBlank(message = "La descripción de la actividad es obligatoria")
    @Pattern(
        regexp = "^$|.{20,}$",
        message = "El descripción de la actividad debe tener al menos 20 caracteres"
    )
    private String descripcion;

    private Long causaId;
    private Long efectoId;
    private Long medioId;
    private Long finId;
    private Long proyectoId;

}
