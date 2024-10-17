package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Optional<Inscripcion> findByConvocatoriaAndProyecto(Convocatoria convocatoria, Proyecto proyecto);
    Optional<Inscripcion> findByProyectoAndEstado(Proyecto proyecto, String estado);

}
