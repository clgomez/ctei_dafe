package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedioRepository extends JpaRepository<Medio, Long> {
    List<Medio> findByArbolDeObjetivosId(Long arbolDeObjetivosId);
    List<Medio> findByArbolDeObjetivos(ArbolDeObjetivos arbolDeObjetivos);
    void deleteByArbolDeObjetivosId(Long arbolDeObjetivosId);


}
