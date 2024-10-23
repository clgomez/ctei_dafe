package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class AsignacionDeRolesDTO {

    private Long id;
    private String estado;
    private String fechaAsignacion;
    private String fechaActualizacion;
    private Long proyectoId;
    private Long usuarioTutorId;
    private Long usuarioEvaluadorId;
}
