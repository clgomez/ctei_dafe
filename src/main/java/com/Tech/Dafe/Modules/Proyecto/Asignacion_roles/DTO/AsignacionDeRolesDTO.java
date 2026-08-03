package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class AsignacionDeRolesDTO {

    private Long id;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaActualizacion;

    @NotBlank(message = "El estado de la asignación de roles es obligatoria")
    @Pattern(
        regexp = "^$|^(ACTIVO|NO ACTIVO)$",
        message = "El estado de la asignación de roles solo puede ser ACTIVO o NO ACTIVO"
    )
    private String estado;

    private Long proyectoId;
    private Long usuarioInvestigadorId;
    private Long usuarioTutorId;
    private Long usuarioEvaluadorId;
}
