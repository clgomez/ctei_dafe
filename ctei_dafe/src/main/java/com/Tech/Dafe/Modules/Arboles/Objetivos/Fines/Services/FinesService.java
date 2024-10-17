package com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.DTO.FinesDTO;

import java.util.List;

public interface FinesService {

    FinesDTO crearFines(FinesDTO finesDTO);

    List<FinesDTO> obtenerTodos();

    FinesDTO obtenerPorId(Long id);

    FinesDTO actualizarMedio(Long id, FinesDTO finesDTO);

    boolean eliminarMedio(Long id);

    List<FinesDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId);

    FinesDTO actualizarCalificacion(Long id, FinesDTO finesDTO);
}
