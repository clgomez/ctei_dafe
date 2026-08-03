package com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Repository;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Models.Fin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinRepository extends JpaRepository<Fin, Long> {
    List<Fin> findByArbolDeObjetivos_Id(Long arbolDeObjetivosId);
    void deleteByArbolDeObjetivosId(Long arbolDeObjetivosId);

}
