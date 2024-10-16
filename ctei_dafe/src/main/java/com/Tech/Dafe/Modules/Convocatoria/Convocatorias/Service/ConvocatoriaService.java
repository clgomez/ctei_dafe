package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Service;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO.ConvocatoriaDTO;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;

import java.util.List;

public interface ConvocatoriaService {

    Convocatoria crearConvocatoria(ConvocatoriaDTO convocatoriaDTO);

    List<Convocatoria> obtenerConvocatorias();

    Convocatoria obtenerConvocatoriaPorId(Long id);

    Convocatoria actualizarConvocatoria(Long id, ConvocatoriaDTO convocatoriaDTO);

    void eliminarConvocatoria(Long id);
}
