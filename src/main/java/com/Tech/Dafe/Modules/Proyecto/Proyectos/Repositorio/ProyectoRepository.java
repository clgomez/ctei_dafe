package com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
        List<Proyecto> findByUsuarioId(Long usuarioId);
        boolean existsByTituloNormalizado(String tituloNormalizado);
        boolean existsByTituloNormalizadoAndIdNot(String tituloNormalizado, Long id);

}
