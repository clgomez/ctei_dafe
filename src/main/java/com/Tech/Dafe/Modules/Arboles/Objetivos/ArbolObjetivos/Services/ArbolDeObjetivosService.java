package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;

import java.util.List;

public interface ArbolDeObjetivosService {

    ArbolDeObjetivosDTO crearArbolDeObjetivos(ArbolDeObjetivosDTO arbolDTO);

    List<ArbolDeObjetivosDTO> obtenerTodos();

    ArbolDeObjetivosDTO obtenerPorId(Long id);

    ArbolDeObjetivosDTO actualizarArbolDeObjetivos(Long id, ArbolDeObjetivosDTO arbolDTO);

    boolean eliminarArbolDeObjetivos(Long id);

    ArbolDeObjetivosDTO obtenerArbolDeObjetivosPorIdProyecto(Long proyectoId); 

}
