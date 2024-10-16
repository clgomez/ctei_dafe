package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;
@Data
public class MedioDTO {
    private Long id;
    private float calificacion;
    private SemaforoEnum semaforo;
    private String comentario;
    private String nombre;
    private Long arbolDeObjetivosId;
}
