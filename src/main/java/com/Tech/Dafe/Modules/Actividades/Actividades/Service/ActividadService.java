package com.Tech.Dafe.Modules.Actividades.Actividades.Service;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;
import java.util.List;


public interface ActividadService {

    ActividadDTO crearActividad(ActividadDTO actividadDTO);

    List<ActividadDTO> obtenerTodas();

    ActividadDTO obtenerPorId(Long id);

    ActividadDTO actualizarActividad(Long id, ActividadDTO actividadDTO);

    void eliminarActividad(Long id);

    List<ActividadDTO> obtenerActividadesPorIdProyecto(Long proyectoId);
}
