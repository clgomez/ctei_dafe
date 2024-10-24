package com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
        List<Proyecto> findByUsuarioId(Long usuarioId);

}
