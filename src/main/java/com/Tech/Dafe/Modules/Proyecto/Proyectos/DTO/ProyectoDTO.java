package com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    private String estado;
    private String fecha_actualizacion;
    private String fecha_creacion;
    private String justificacion;
    private String observaciones;
    private String poblacion_objetivo;
    private String presupuesto;
    private String resultados_esperados;

    @NotBlank
    private Long idUsuario;


}
