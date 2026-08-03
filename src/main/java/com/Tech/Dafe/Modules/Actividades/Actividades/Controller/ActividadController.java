package com.Tech.Dafe.Modules.Actividades.Actividades.Controller;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;
import com.Tech.Dafe.Modules.Actividades.Actividades.Service.ActividadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/actividades")
@CrossOrigin
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    @PostMapping
    public ResponseEntity<?> crearActividad(@Valid @RequestBody ActividadDTO actividadDTO) {

        ActividadDTO actividad = actividadService.crearActividad(actividadDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Actividad creada exitosamente",
                        "actividad", actividad
                ));
    }

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> obtenerTodas() {
        return ResponseEntity.ok(actividadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarActividad(
            @PathVariable Long id,
            @Valid @RequestBody ActividadDTO actividadDTO) {

        ActividadDTO actualizada = actividadService.actualizarActividad(id, actividadDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", " Actividad actualizada exitosamente",
                "actividad", actualizada
              )
        );


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarActividad(@PathVariable Long id) {
        actividadService.eliminarActividad(id);
    
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Actividad eliminada exitosamente"
                )
        );
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ActividadDTO>> obtenerActividadesPorIdProyecto(
            @PathVariable Long proyectoId) {

        return ResponseEntity.ok(
                actividadService.obtenerActividadesPorIdProyecto(proyectoId)
        );
    }
}