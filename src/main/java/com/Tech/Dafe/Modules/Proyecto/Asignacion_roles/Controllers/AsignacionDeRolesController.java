package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Controllers;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services.AsignacionDeRolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/asignacion-roles")
@RequiredArgsConstructor
public class AsignacionDeRolesController {

    private final AsignacionDeRolesService asignacionDeRolesService;

    @PostMapping
    public ResponseEntity<?> crearAsignacion(
            @Valid @RequestBody AsignacionDeRolesDTO asignacion) {

        AsignacionDeRolesDTO nuevaAsignacion = asignacionDeRolesService.crearAsignacion(asignacion);

         return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Asignación de roles creada exitosamente",
                        "asignacion", nuevaAsignacion
        ));

    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDeRolesDTO> obtenerAsignacion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                asignacionDeRolesService.obtenerAsignacion(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asignación no encontrada con ID: " + id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAsignacion(
            @PathVariable Long id,
            @Valid @RequestBody AsignacionDeRolesDTO asignacion) {
        
        AsignacionDeRolesDTO actualizada = asignacionDeRolesService.actualizarAsignacion(id, asignacion);
  
        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Asignación de roles actualizada exitosamente",
                "asignacion", actualizada
         )
        );      
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAsignacion(
            @PathVariable Long id) {

        asignacionDeRolesService.eliminarAsignacion(id);
    
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Asignación de roles eliminada exitosamente"
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<AsignacionDeRolesDTO>> listarAsignaciones() {

        return ResponseEntity.ok(
                asignacionDeRolesService.listarAsignaciones()
        );
    }

    @GetMapping("/usuarioinvestigador/{usuarioInvestigadorId}")
    public ResponseEntity<List<AsignacionDeRolesDTO>>
    obtenerAsignacionesDeRolesPorIdUsuarioInvestigador(
            @PathVariable Long usuarioInvestigadorId) {

        return ResponseEntity.ok(
                asignacionDeRolesService
                        .obtenerAsignacionesDeRolesPorIdUsuarioInvestigador(
                                usuarioInvestigadorId)
        );
    }

    @GetMapping("/usuariotutor/{usuarioTutorId}")
    public ResponseEntity<List<AsignacionDeRolesDTO>>
    obtenerAsignacionesDeRolesPorIdUsuarioTutor(
            @PathVariable Long usuarioTutorId) {

        return ResponseEntity.ok(
                asignacionDeRolesService
                        .obtenerAsignacionesDeRolesPorIdUsuarioTutor(
                                usuarioTutorId)
        );
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<AsignacionDeRolesDTO>
    obtenerAsignacionDeRolesPorIdProyecto(
            @PathVariable Long proyectoId) {

        return ResponseEntity.ok(
                asignacionDeRolesService
                        .obtenerAsignacionDeRolesPorIdProyecto(proyectoId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una asignación para el proyecto con ID: "
                                                + proyectoId))
        );
    }

}