package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ArbolDeProblemasDTO {

        private Long id;

        @NotBlank(message = "La descripción del árbol de problemas es obligatoria")
        @Pattern(
          regexp = "^$|.{10,}$",
          message = "La descripción del árbol de problemas debe tener al menos 10 caracteres"
        )
        private String descripcion;
        
        private SemaforoEnum semaforo;
        private Long proyectoId;

        public void setSemaforo(SemaforoEnum semaforo) {
                this.semaforo = semaforo;
        }

}
