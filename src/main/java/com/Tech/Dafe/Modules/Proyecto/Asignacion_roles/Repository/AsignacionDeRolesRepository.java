package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Repository;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models.AsignacionDeRoles;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsignacionDeRolesRepository extends JpaRepository<AsignacionDeRoles, Long> {
    List<AsignacionDeRoles> findByUsuarioInvestigadorId(Long usuarioInvestigadorId);
    List<AsignacionDeRoles> findByUsuarioTutorId(Long usuarioTutorId);
    Optional<AsignacionDeRoles> findByProyectoId(Long ProyectoId);
    Optional<AsignacionDeRoles> findByProyectoIdAndUsuarioInvestigadorIdAndUsuarioTutorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioTutorId);
    Optional<AsignacionDeRoles> findByProyectoIdAndUsuarioInvestigadorIdAndUsuarioEvaluadorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioEvaluadorId);

    
    // =========================
    // ELIMINAR (INVESTIGADOR)
    // =========================
    @Transactional
    @Modifying
    @Query("DELETE FROM AsignacionDeRoles a WHERE a.usuarioInvestigador.id = :id")
    void deleteByUsuarioInvestigadorId(@Param("id") Long id);

    // =========================
    // DESVINCULAR TUTOR
    // =========================
    @Modifying
    @Transactional
    @Query("UPDATE AsignacionDeRoles a SET a.usuarioTutor = null WHERE a.usuarioTutor.id = :id")
    void desvincularTutor(@Param("id") Long id);

    // =========================
    // DESVINCULAR EVALUADOR
    // =========================
    @Modifying
    @Transactional
    @Query("UPDATE AsignacionDeRoles a SET a.usuarioEvaluador = null WHERE a.usuarioEvaluador.id = :id")
    void desvincularEvaluador(@Param("id") Long id);

    // =========================
    // MÉTODO GENERAL
    // =========================
    default void desvincularUsuario(Long usuarioId) {
        desvincularTutor(usuarioId);
        desvincularEvaluador(usuarioId);
    }

}
