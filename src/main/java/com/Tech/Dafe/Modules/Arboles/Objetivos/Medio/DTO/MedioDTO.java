package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class MedioDTO {
    private Long id;

    @NotBlank(message = "La descripción del medio es obligatoria")
    @Pattern(
        regexp = "^$|.{5,}$",
        message = "La descripción del medio debe tener al menos 5 caracteres"
    )
    private String descripcion;

    private SemaforoEnum semaforo;
    private Long arbolDeObjetivosId;
}
