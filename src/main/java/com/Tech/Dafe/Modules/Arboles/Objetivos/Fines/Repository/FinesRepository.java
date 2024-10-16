package com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Repository;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinesRepository extends JpaRepository<Fines, Long> {
    List<Fines> findByArbolDeObjetivos_Id(Long arbolDeObjetivosId);

}
