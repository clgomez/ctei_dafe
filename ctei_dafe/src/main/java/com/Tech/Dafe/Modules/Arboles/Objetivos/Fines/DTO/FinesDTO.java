package com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;

@Data
public class FinesDTO {

    private Long id;
    private String nombre;
    private float calificacion;
    private String comentario;
    private SemaforoEnum semaforo;

    private Long arbolDeObjetivosId;

}
