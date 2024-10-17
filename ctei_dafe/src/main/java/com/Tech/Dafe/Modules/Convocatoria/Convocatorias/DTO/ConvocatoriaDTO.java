package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConvocatoriaDTO {

    private String descripcion;
    private String estado;
    private String fechaFin;
    private String fechaInicio;
}
