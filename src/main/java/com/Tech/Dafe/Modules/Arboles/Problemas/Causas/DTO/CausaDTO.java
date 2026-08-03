package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CausaDTO {

    private Long id;

    @NotBlank(message = "La descripción de la causa es obligatoria")
    @Pattern(
        regexp = "^$|.{5,}$",
        message = "La descripción de la causa debe tener al menos 5 caracteres"
    )
    private String descripcion;

    private SemaforoEnum semaforo;
    private Long arbolDeProblemasId;

}
