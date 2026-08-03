package com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.DTO.FinDTO;

import java.util.List;

public interface FinService {

    FinDTO crearFin(FinDTO finDTO);

    List<FinDTO> obtenerTodos();

    FinDTO obtenerPorId(Long id);

    FinDTO actualizarFin(Long id, FinDTO finDTO);

    boolean eliminarFin(Long id);

    List<FinDTO> obtenerFinesPorArbolDeObjetivosId(Long arbolDeObjetivosId);

}
