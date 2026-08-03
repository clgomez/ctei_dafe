package com.Tech.Dafe.Modules.Calificaciones.Controller;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Calificaciones.DTO.CalificacionDTO;
import com.Tech.Dafe.Modules.Calificaciones.DTO.RubroEvaluacionDTO;
import com.Tech.Dafe.Modules.Calificaciones.Service.CalificacionService;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.RolCalificador;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calificaciones")
@CrossOrigin
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;
    
    private final UsuarioRepository usuarioRepository;

    /**
     * Crear calificación completa
     */
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody CalificacionDTO dto) {

        CalificacionDTO calificacion = calificacionService.crearCalificacion(dto);

         return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Calificación creada exitosamente",
                        "calificacion", calificacion
                ));
    }

    /**
     * Actualizar calificación
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody CalificacionDTO dto) {

        CalificacionDTO actualizada =
                calificacionService.actualizarCalificacion(id, dto);
   
        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Calificación actualizada exitosamente",
                "calificacion", actualizada
        )
     );
    }

    /**
     * Obtener calificación completa
     */
   @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> obtener(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionCompleta(id));
    }

    /**
     * Obtener rúbrica base del proyecto
     */
    @GetMapping("/rubrica/{proyectoId}")
    public ResponseEntity<List<RubroEvaluacionDTO>> obtenerRubrica(
            @PathVariable Long proyectoId) {

        return ResponseEntity.ok(
                calificacionService.obtenerRubricaProyecto(proyectoId));
    }

    /**
     * Obtener todas las calificaciones
     */
    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listarTodas() {

        return ResponseEntity.ok(
                calificacionService.obtenerTodas());
    }


    // =====================================================
    // ELIMINAR COMPLETAMENTE UNA CALIFICACION
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCalificacion(
            @PathVariable Long id) {

        calificacionService.eliminar(id);

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje","Calificación eliminada correctamente"
                )
        );

    }

    // =====================================================
    // ELIMINAR EVALUACIONES DEL TUTOR
    // =====================================================

    @DeleteMapping("/{id}/tutor")
    public ResponseEntity<?> eliminarEvaluacionesTutor(
            @PathVariable Long id) {

        calificacionService.eliminarEvaluacionesPorRol(
                id,
                RolCalificador.TUTOR
        );

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje","Evaluaciones del tutor eliminadas correctamente"
                )
        );
    }

    // =====================================================
    // ELIMINAR EVALUACIONES DEL EVALUADOR
    // =====================================================

    @DeleteMapping("/{id}/evaluador")
    public ResponseEntity<?> eliminarEvaluacionesEvaluador(
            @PathVariable Long id) {

        calificacionService.eliminarEvaluacionesPorRol(
                id,
                RolCalificador.EVALUADOR
        );

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje","Evaluaciones del evaluador eliminadas correctamente"
                )
        );
  
    }

    @GetMapping("/usuarioinvestigador/{usuarioInvestigadorId}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorIdUsuarioInvestigador(
            @PathVariable Long usuarioInvestigadorId) {

        if (!usuarioRepository.existsById(usuarioInvestigadorId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el usuario investigador con ID: "
                            + usuarioInvestigadorId);
        }

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionesPorIdUsuarioInvestigador(
                        usuarioInvestigadorId));
    }

    @GetMapping("/usuariotutor/{usuarioTutorId}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorIdUsuarioTutor(
            @PathVariable Long usuarioTutorId) {

        if (!usuarioRepository.existsById(usuarioTutorId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el usuario tutor con ID: "
                            + usuarioTutorId);
        }

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionesPorIdUsuarioTutor(
                        usuarioTutorId));
    }

    @GetMapping("/usuarioevaluador/{usuarioEvaluadorId}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorIdUsuarioEvaluador(
            @PathVariable Long usuarioEvaluadorId) {

        if (!usuarioRepository.existsById(usuarioEvaluadorId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el usuario evaluador con ID: "
                            + usuarioEvaluadorId);
        }

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionesPorIdUsuarioEvaluador(
                        usuarioEvaluadorId));
    }

    
}