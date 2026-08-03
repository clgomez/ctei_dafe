package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Optional<Inscripcion> findByConvocatoriaAndProyecto(Convocatoria convocatoria, Proyecto proyecto);
    Optional<Inscripcion> findByProyectoAndEstado(Proyecto proyecto, String estado);
    List<Inscripcion> findByUsuarioId(Long usuarioId);
    Optional<Inscripcion> findByProyectoIdAndUsuarioId(Long proyectoId, Long usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Inscripcion i WHERE i.usuario.id = :usuarioId")
    void deleteByUsuarioId(Long usuarioId);

}
