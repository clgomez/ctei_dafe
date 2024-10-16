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
    private String fechaActualizacion;
    private String fechaCreacion;
    private String justificacion;
    private String observaciones;
    private String poblacionObjetivo;
    private String presupuesto;
    private String resultadosEsperados;

    @NotBlank
    private Long idUsuario;


}
