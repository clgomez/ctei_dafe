package com.Tech.Dafe.Modules.Actividades.Cronograma.Repository;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
}