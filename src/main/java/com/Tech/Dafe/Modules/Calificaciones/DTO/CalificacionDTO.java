package com.Tech.Dafe.Modules.Calificaciones.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.EstadoCalificacion;

@Data
public class CalificacionDTO {

    private Long id;

    // Proyecto evaluado
    private Long proyectoId;

    // Usuarios participantes
    private Long tutorId;

    private Long evaluadorId;

    private Long investigadorId;


    // Notas tutor/evaluador

    private Float notaTutor;

    private Float notaEvaluador;

    // Resultado global
    private Float calificacionFinal;

    private EstadoCalificacion estado;

    private LocalDateTime fechaCreacion;

    // Historico de tutor/evaluador
    private Long idTutorHistorico;

    private Long idEvaluadorHistorico;

    private String nombreTutorHistorico;

    private String nombreEvaluadorHistorico;


     // Rúbrica completa
    private List<RubroCalificacionDTO> rubros;
}