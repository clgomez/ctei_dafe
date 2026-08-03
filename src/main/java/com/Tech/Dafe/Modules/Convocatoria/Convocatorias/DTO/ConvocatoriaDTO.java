package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConvocatoriaDTO {

    @NotBlank(message = "El título de la convocatoria es obligatorio")
    //@Size(min = 5, message = "El título debe tener al menos 5 caracteres")
    @Pattern(
        regexp = "^$|.{5,}$",
        message = "El título de la convocatoria debe tener al menos 5 caracteres"
    )
    private String titulo;

    @NotBlank(message = "La descripción de la convocatoria es obligatoria")
    //@Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    @Pattern(
        regexp = "^$|.{10,}$",
        message = "La descripción de la convocatoria debe tener al menos 10 caracteres"
    )
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio de la convocatoria es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha fin de la convocatoria es obligatoria")
    private LocalDate fechaFin;
 
    @NotBlank(message = "El estado de la convocatoria es obligatoria")
    @Pattern(
        regexp = "^$|^(ACTIVO|NO ACTIVO)$",
        message = "El estado de la convocatoria solo puede ser ACTIVO o NO ACTIVO"
    )
    private String estado;

    private String tituloNormalizado;
}
