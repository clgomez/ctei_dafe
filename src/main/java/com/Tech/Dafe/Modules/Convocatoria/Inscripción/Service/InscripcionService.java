package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;

import java.util.List;

public interface InscripcionService {

    Inscripcion inscribirProyecto(InscripcionProyectoDTO dto);

    Inscripcion actualizarInscripcion(Long inscripcionId, InscripcionProyectoDTO dto);

    void eliminarInscripcion(Long inscripcionId);

    List<Inscripcion> obtenerTodasInscripciones();
}
