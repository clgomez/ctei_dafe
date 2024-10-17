package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolProblemasDTO;

import java.util.List;

public interface ArbolDeProblemasServices {

    ArbolProblemasDTO crearArbolDeProblemas(ArbolProblemasDTO arbolDTO, Long proyectoId);

    ArbolProblemasDTO obtenerPorId(Long id);

    boolean eliminarArbolDeProblemas(Long id);

    List<ArbolProblemasDTO> obtenerTodos();

    ArbolProblemasDTO actualizarArbolDeProblemas(Long id, ArbolProblemasDTO arbolDTO);

    ArbolProblemasDTO actualizarCalificacion(Long id, ArbolProblemasDTO arbolDTO);
}
