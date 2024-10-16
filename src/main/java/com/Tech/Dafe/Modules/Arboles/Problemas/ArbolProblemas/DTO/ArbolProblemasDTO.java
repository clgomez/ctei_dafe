package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;

@Data
public class ArbolProblemasDTO {

        private Long id;
        private String nombre;
        private String descripcion;
        private String comentario;
        private float calificacion;
        private SemaforoEnum semaforo;
        private Long proyectoId;

        public void setCalificacion(float calificacion) {
                this.calificacion = calificacion;
        }

        public void setSemaforo(SemaforoEnum semaforo) {
                this.semaforo = semaforo;
        }

}
