package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;

@Data
public class ArbolDeObjetivosDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private float calificacion;
    private String comentario;
    private SemaforoEnum semaforo;
    private Long proyectoId;

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public void setSemaforo(SemaforoEnum semaforo) {
        this.semaforo = semaforo;
    }
}
