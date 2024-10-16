package com.Tech.Dafe.Modules.Actividades.Actividades.DTO;

import lombok.Data;

@Data
public class ActividadDTO {

    private Long id;
    private String nombre;
    private String descripcion;

    private Long proyectoId;
    private Long efectoId;
    private Long finId;
    private Long causaId;
    private Long medioId;

}
