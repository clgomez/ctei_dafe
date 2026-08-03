package com.Tech.Dafe.Modules.Actividades.Cronograma.DTO;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CronogramaDTO {

    private Long id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha fin es obligatoria")
    private LocalDate fechaFin;
    private Long actividadId;
    
}








