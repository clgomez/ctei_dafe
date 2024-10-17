package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;

import java.util.List;

public interface EfectoService {

    EfectoDTO crearEfecto(EfectoDTO efectoDTO);

    List<EfectoDTO> obtenerTodos();

    EfectoDTO obtenerPorId(Long id);

    EfectoDTO actualizarMedio(Long id, EfectoDTO efectoDTO);

    boolean eliminarMedio(Long id);

    List<EfectoDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId);

    EfectoDTO actualizarCalificacion(Long id, EfectoDTO efectoDTO);
}
