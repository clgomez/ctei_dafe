package com.Tech.Dafe.Modules.Actividades.Actividades.Repository;

import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}