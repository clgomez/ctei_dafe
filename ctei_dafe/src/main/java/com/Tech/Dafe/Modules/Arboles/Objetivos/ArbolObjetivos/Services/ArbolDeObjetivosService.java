package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;

import java.util.List;
import java.util.Optional;

public interface ArbolDeObjetivosService {

    ArbolDeObjetivosDTO crearArbolDeObjetivos(ArbolDeObjetivosDTO arbolDTO);

    List<ArbolDeObjetivosDTO> obtenerTodos();

    Optional<ArbolDeObjetivosDTO> obtenerPorId(Long id);

    ArbolDeObjetivosDTO actualizarArbolDeObjetivos(Long id, ArbolDeObjetivosDTO arbolDTO);

    ArbolDeObjetivosDTO actualizarCalificacion(Long id, ArbolDeObjetivosDTO arbolDTO);

    boolean eliminarArbolDeObjetivos(Long id);
}
