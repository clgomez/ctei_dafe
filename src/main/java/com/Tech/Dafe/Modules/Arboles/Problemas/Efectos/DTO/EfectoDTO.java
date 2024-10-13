package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;

@Data
public class EfectoDTO {

    private Long id;
    private float calificacion;
    private SemaforoEnum semaforo;
    private String nombre;
    private String comentario;


    private Long arbolDeProblemasId;

}
