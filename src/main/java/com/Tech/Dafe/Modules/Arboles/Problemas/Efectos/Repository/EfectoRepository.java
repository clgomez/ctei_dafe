package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository;

import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EfectoRepository extends JpaRepository<Efecto, Long> {
    List<Efecto> findByArbolProblemas_Id(Long arbolDeProblemasId);

}
