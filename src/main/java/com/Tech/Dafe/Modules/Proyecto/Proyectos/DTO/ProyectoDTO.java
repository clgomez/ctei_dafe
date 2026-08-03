package com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {

    @NotBlank(message = "El título es obligatorio")
    //@Size(min = 5, message = "El título debe tener al menos 5 caracteres")
    @Pattern(
        regexp = "^$|.{5,}$",
        message = "El título debe tener al menos 5 caracteres"
    )
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    //@Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    @Pattern(
        regexp = "^$|.{10,}$",
        message = "La descripción debe tener al menos 10 caracteres"
    )
    private String descripcion;

    @NotBlank(message = "La población objetivo es obligatoria")
    private String poblacionObjetivo;
    
    @NotBlank(message = "La justificación es obligatoria")
    //@Size(min = 10, message = "La justificación debe tener al menos 10 caracteres")
    @Pattern(
        regexp = "^$|.{10,}$",
        message = "La justificación debe tener al menos 10 caracteres"
    )
    private String justificacion;
    
    @NotNull(message = "El presupuesto es obligatorio")
    @Positive(message = "El presupuesto debe ser mayor que cero")
    private Float presupuesto;

    @NotBlank(message = "Los resultados esperados son obligatorios")
    //@Size(min = 10, message = "Los resultados esperados deben tener al menos 10 caracteres")
    @Pattern(
        regexp = "^$|.{10,}$",
        message = "Los resultados esperados deben tener al menos 10 caracteres"
    )
    private String resultadosEsperados;
    
    //@Size(max = 300, message = "Las observaciones no deben exceder de 300 caracteres")
    @Pattern(
        regexp = "^$|^.{1,300}$",
        message = "Las observaciones no deben exceder de 300 caracteres"
    )
    private String observaciones;

    @NotBlank(message = "El estado del usuario es obligatorio")
    @Pattern(
        regexp = "^$|^(ACTIVO|NO ACTIVO)$",
        message = "El estado del usuario solo puede ser ACTIVO o NO ACTIVO"
    )
    private String estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    private String tituloNormalizado;

    private Long idUsuario;


}
