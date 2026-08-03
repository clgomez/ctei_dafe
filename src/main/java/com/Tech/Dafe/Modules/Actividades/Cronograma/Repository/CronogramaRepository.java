package com.Tech.Dafe.Modules.Actividades.Cronograma.Repository;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {

    Optional<Cronograma> findByActividadId(Long actividadId);
}