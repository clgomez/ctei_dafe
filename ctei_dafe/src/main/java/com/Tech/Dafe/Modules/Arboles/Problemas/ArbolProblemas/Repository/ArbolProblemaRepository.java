package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository;


import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArbolProblemaRepository extends JpaRepository<ArbolDeProblemas, Long> {
    boolean existsByProyectoId(Long proyectoId);
}
