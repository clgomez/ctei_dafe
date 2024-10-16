package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedioRepository extends JpaRepository<Medio, Long> {
    List<Medio> findByArbolDeObjetivos_Id(Long arbolDeObjetivosId);
    List<Medio> findByArbolDeObjetivos(ArbolDeObjetivos arbolDeObjetivos);


}
