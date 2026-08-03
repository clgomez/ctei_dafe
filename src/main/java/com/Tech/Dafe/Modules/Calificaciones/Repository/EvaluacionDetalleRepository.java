package com.Tech.Dafe.Modules.Calificaciones.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.Tech.Dafe.Modules.Calificaciones.Models.EvaluacionDetalle;

public interface EvaluacionDetalleRepository extends JpaRepository<EvaluacionDetalle, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE EvaluacionDetalle e
        SET e.idUsuarioHistorico = :idHistorico,
            e.nombreUsuarioHistorico = :nombreHistorico
        WHERE e.usuario.id = :usuarioId
    """)
    void guardarDatosHistoricos(
            @Param("usuarioId") Long usuarioId,
            @Param("idHistorico") Long idHistorico,
            @Param("nombreHistorico") String nombreHistorico
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE EvaluacionDetalle e
        SET e.usuario = NULL
        WHERE e.usuario.id = :usuarioId
    """)
    void desvincularUsuario(@Param("usuarioId") Long usuarioId);
    
}