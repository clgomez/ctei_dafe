package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Controller;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service.InscripcionService;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;

import jakarta.validation.Valid;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> inscribirProyecto(
            @Valid @RequestBody InscripcionProyectoDTO dto) {

        Inscripcion nuevaInscripcion = inscripcionService.inscribirProyecto(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Inscripción creado exitosamente",
                        "inscripcion", nuevaInscripcion
                ));
    }

    @PutMapping("/{inscripcionId}")
    public ResponseEntity<?> actualizarInscripcion(
            @PathVariable Long inscripcionId,
            @Valid @RequestBody InscripcionProyectoDTO dto) {

        
        Inscripcion actualizada = inscripcionService.actualizarInscripcion(inscripcionId, dto);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Proyecto actualizado exitosamente",
                "inscripcion", actualizada
        )
     );        
    }

    @DeleteMapping("/{inscripcionId}")
    public ResponseEntity<?> eliminarInscripcion(
            @PathVariable Long inscripcionId) {

        inscripcionService.eliminarInscripcion(inscripcionId);
        
         return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Inscripción eliminada exitosamente"
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripciones() {

        return ResponseEntity.ok(
                inscripcionService.obtenerTodasInscripciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionProyectoDTO> ver(@PathVariable Long id) {

        Inscripcion inscripcion = inscripcionService.obtenerInscripcion(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inscripción no encontrada con ID: " + id));

        InscripcionProyectoDTO dto = new InscripcionProyectoDTO(null, null, null);

        dto.setProyectoId(inscripcion.getProyecto().getId());
        dto.setConvocatoriaId(inscripcion.getConvocatoria().getId());
        dto.setEstado(inscripcion.getEstado());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorIdUsuario(
            @PathVariable Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el usuario con ID: " + usuarioId);
        }

        return ResponseEntity.ok(
                inscripcionService.obtenerInscripcionesPorIdUsuario(usuarioId));
    }


   @GetMapping("/proyecto/{proyectoId}/usuarioinvestigador/{usuarioInvestigadorId}")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorIdProyectoYIdUsuarioInvestigador(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioInvestigadorId) {

        return ResponseEntity.ok(
                inscripcionService.obtenerInscripcionPorIdProyectoYIdUsuarioInvestigador(
                        proyectoId,
                        usuarioInvestigadorId));
    }

}
