package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;

import java.util.List;

public interface EfectoService {

    EfectoDTO crearEfecto(EfectoDTO efectoDTO);

    List<EfectoDTO> obtenerTodos();

    EfectoDTO obtenerPorId(Long id);

    EfectoDTO actualizarEfecto(Long id, EfectoDTO efectoDTO);

    boolean eliminarEfecto(Long id);

    List<EfectoDTO> obtenerEfectosPorArbolDeProblemasId(Long arbolDeProblemasId);

}
