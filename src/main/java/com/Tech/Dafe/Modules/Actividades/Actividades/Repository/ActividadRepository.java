package com.Tech.Dafe.Modules.Actividades.Actividades.Repository;

import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByProyectoId(Long proyectoId);
}