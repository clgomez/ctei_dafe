package com.Tech.Dafe.Modules.Actividades.Cronograma.Service;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;

import java.util.List;
import java.util.Optional;

public interface CronogramaService {

    Cronograma crearCronograma(Cronograma cronograma);

    List<Cronograma> obtenerTodos();

    Optional<Cronograma> obtenerPorId(Long id);

    Cronograma actualizarCronograma(Long id, Cronograma cronograma);

    boolean eliminarCronograma(Long id);
}
