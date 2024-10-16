package com.Tech.Dafe.Modules.Actividades.Actividades.Service;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;

import java.util.List;
import java.util.Optional;

public interface ActividadService {

    ActividadDTO crearActividad(ActividadDTO actividadDTO);

    List<ActividadDTO> obtenerTodas();

    Optional<ActividadDTO> obtenerPorId(Long id);

    ActividadDTO actualizarActividad(Long id, ActividadDTO actividadDTO);

    boolean eliminarActividad(Long id);
}
