package com.Tech.Dafe.Modules.Calificaciones.Repository;

import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    //List<Calificacion> findByProyectoId(Long proyectoId);
    Optional<Calificacion> findByProyectoId(Long proyectoId);
    List<Calificacion> findByUsuarioInvestigadorId(Long usuarioInvestigadorId);
    List<Calificacion> findByUsuarioInvestigadorIdAndUsuarioTutorIdIsNotNull(Long usuarioInvestigadorId);
    List<Calificacion> findByUsuarioTutorId(Long usuarioTutorId);
    List<Calificacion> findByUsuarioEvaluadorId(Long usuarioEvaluadorId);

   // =========================
    // ELIMINAR (SOLO INVESTIGADOR)
    // =========================
    /*@Modifying
    @Query("DELETE FROM Calificacion c WHERE c.usuarioInvestigador.id = :usuarioId")
    void deleteByInvestigadorId(@Param("usuarioId") Long usuarioId);
    */


    // =========================
    // GUARDAR HISTORICO TUTOR
    // =========================
    @Modifying
    @Query("""
        UPDATE Calificacion c
        SET c.idTutorHistorico = :idHistorico,
            c.nombreTutorHistorico = :nombre
        WHERE c.usuarioTutor.id = :usuarioId
    """)
    void guardarHistoricoTutor(
        Long usuarioId,
        Long idHistorico,
        String nombre
    );

    // =========================
    // GUARDAR HISTORICO EVALUADOR
    // =========================
    @Modifying
    @Query("""
        UPDATE Calificacion c
        SET c.idEvaluadorHistorico = :idHistorico,
            c.nombreEvaluadorHistorico = :nombre
        WHERE c.usuarioEvaluador.id = :usuarioId
    """)
    void guardarHistoricoEvaluador(
        Long usuarioId,
        Long idHistorico,
        String nombre
    );

    // =========================
    // DESVINCULAR TUTOR
    // =========================
    @Modifying
    @Query("UPDATE Calificacion c SET c.usuarioTutor = null WHERE c.usuarioTutor.id = :usuarioId")
    void desvincularTutor(@Param("usuarioId") Long usuarioId);
    
   
    // =========================
    // DESVINCULAR EVALUADOR
    // =========================
    @Modifying
    @Query("UPDATE Calificacion c SET c.usuarioEvaluador = null WHERE c.usuarioEvaluador.id = :usuarioId")
    void desvincularEvaluador(@Param("usuarioId") Long usuarioId);
    

    // =========================
    // MÉTODO GENERAL
    // =========================
    default void desvincularUsuario(Long usuarioId) {
        desvincularTutor(usuarioId);
        desvincularEvaluador(usuarioId);
    }

    @EntityGraph(attributePaths = {
        "rubros",
        "rubros.evaluaciones",
        "rubros.evaluaciones.usuario"
    })
    Optional<Calificacion> findCompleteById(Long id);


    
    
}
