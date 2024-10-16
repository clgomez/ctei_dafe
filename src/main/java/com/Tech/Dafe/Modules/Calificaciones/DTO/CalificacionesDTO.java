package com.Tech.Dafe.Modules.Calificaciones.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalificacionesDTO {

        private Float calificacionDescripcion;
        private Float calificacionFinal;
        private Float calificacionJustificacion;
        private Float calificacionPoblacionObjetivo;
        private Float calificacionPresupuesto;
        private Float calificacionTitulo;
        private String comentario;
        private String estado;
        private Float resultadoEsperado;
        private Long IdInscripcion;

        @NotNull
        private Long proyectoId;

        @NotNull
        private Long efectoId;

        @NotNull
        private Long finId;

        @NotNull
        private Long arbolDeProblemasId;

        @NotNull
        private Long causaId;

        @NotNull
        private Long medioId;

        @NotNull
        private Long cronogramaId;

        @NotNull
        private SemaforoEnum semaforo;
}
