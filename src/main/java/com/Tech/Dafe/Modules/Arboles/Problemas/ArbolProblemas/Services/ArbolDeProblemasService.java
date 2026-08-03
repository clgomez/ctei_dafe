package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolDeProblemasDTO;
import java.util.*;


public interface ArbolDeProblemasService {

    ArbolDeProblemasDTO crearArbolDeProblemas(ArbolDeProblemasDTO arbolDTO, Long proyectoId);

    ArbolDeProblemasDTO obtenerPorId(Long id);

    boolean eliminarArbolDeProblemas(Long id);

    List<ArbolDeProblemasDTO> obtenerTodos();

    ArbolDeProblemasDTO actualizarArbolDeProblemas(Long id, ArbolDeProblemasDTO arbolDTO);

    ArbolDeProblemasDTO obtenerArbolDeProblemasPorIdProyecto(Long proyectoId); 

}
