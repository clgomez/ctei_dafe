package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;

import java.util.List;

public interface CausaService {

    CausaDTO crearCausa(CausaDTO causaDTO);

    List<CausaDTO> obtenerTodos();

    CausaDTO obtenerPorId(Long id);

    CausaDTO actualizarMedio(Long id, CausaDTO causaDTO);

    boolean eliminarMedio(Long id);

    List<CausaDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId);

    CausaDTO actualizarCalificacion(Long id, CausaDTO causaDTO);
}
