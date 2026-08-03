package com.Tech.Dafe.Modules.Actividades.Cronograma.Service;

import com.Tech.Dafe.Modules.Actividades.Cronograma.DTO.CronogramaDTO;

import java.util.List;


public interface CronogramaService {

    CronogramaDTO crearCronograma(CronogramaDTO cronogramaDTO);

    List<CronogramaDTO> obtenerTodos();

    CronogramaDTO obtenerPorId(Long id);

    CronogramaDTO actualizarCronograma(Long id, CronogramaDTO cronogramaDTO);

    void eliminarCronograma(Long id);

    CronogramaDTO obtenerCronogramaPorIdActividad(Long actividadId); 
}
