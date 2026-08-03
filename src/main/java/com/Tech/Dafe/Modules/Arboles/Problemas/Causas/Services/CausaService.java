package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;

import java.util.List;

public interface CausaService {

    CausaDTO crearCausa(CausaDTO causaDTO);

    List<CausaDTO> obtenerTodos();

    CausaDTO obtenerPorId(Long id);

    CausaDTO actualizarCausa(Long id, CausaDTO causaDTO);

    boolean eliminarCausa(Long id);

    List<CausaDTO> obtenerCausasPorArbolDeProblemasId(Long arbolDeProblemasId);
    
}
