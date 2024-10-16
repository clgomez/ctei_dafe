package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;

import java.util.List;

public interface MedioService {

    MedioDTO crearMedio(MedioDTO medioDTO);

    List<MedioDTO> obtenerTodos();

    MedioDTO obtenerPorId(Long id);

    MedioDTO actualizarMedio(Long id, MedioDTO medioDTO);

    boolean eliminarMedio(Long id);

    List<MedioDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId);

    MedioDTO actualizarCalificacion(Long id, MedioDTO medioDTO);
}
