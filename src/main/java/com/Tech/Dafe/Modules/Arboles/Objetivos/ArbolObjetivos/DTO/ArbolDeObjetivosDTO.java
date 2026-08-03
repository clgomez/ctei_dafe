package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ArbolDeObjetivosDTO {
    private Long id;

    @NotBlank(message = "La descripción del árbol de objetivos es obligatoria")
    @Pattern(
          regexp = "^$|.{10,}$",
          message = "La descripción del árbol de objetivos debe tener al menos 10 caracteres"
        )
    private String descripcion;

    private SemaforoEnum semaforo;
    private Long proyectoId;

    public void setSemaforo(SemaforoEnum semaforo) {
        this.semaforo = semaforo;
    }
}
