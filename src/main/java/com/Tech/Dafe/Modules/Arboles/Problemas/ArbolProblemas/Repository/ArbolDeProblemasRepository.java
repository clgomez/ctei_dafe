package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArbolDeProblemasRepository extends JpaRepository<ArbolDeProblemas, Long> {
    boolean existsByProyectoId(Long proyectoId);
    Optional<ArbolDeProblemas> findByProyectoId(Long proyectoId);

}
