package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CausaRepository extends JpaRepository<Causa, Long> {
    List<Causa> findByArbolDeProblemas_Id(Long arbolDeProblemasId);

}
