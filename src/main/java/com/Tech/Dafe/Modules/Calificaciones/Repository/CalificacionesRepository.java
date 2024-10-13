package com.Tech.Dafe.Modules.Calificaciones.Repository;

import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionesRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByProyectoId(Long proyectoId);

}
