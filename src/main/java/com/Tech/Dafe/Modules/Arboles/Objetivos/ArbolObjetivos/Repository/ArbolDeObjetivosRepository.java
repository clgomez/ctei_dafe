package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArbolDeObjetivosRepository extends JpaRepository<ArbolDeObjetivos, Long> {
    boolean existsByProyectoId(Long proyectoId);
    Optional<ArbolDeObjetivos> findByProyectoId(Long proyectoId);

}
