package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO;

import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import lombok.Data;

@Data
public class CausaDTO {

    private Long id;
    private float calificacion;
    private SemaforoEnum semaforo;
    private String comentario;
    private String nombre;
    private Long arbolDeProblemasId;

}
