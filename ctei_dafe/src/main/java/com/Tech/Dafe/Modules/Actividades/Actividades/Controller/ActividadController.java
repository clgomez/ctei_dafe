package com.Tech.Dafe.Modules.Actividades.Actividades.Controller;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;
import com.Tech.Dafe.Modules.Actividades.Actividades.Service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @PostMapping
    public ResponseEntity<ActividadDTO> crearActividad(@RequestBody ActividadDTO actividadDTO) {
        ActividadDTO nuevaActividad = actividadService.crearActividad(actividadDTO);
        return ResponseEntity.ok(nuevaActividad);
    }

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> obtenerTodas() {
        List<ActividadDTO> actividades = actividadService.obtenerTodas();
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtenerPorId(@PathVariable Long id) {
        return actividadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> actualizarActividad(@PathVariable Long id, @RequestBody ActividadDTO actividadDTO) {
        ActividadDTO actividadActualizada = actividadService.actualizarActividad(id, actividadDTO);
        return actividadActualizada != null
                ? ResponseEntity.ok(actividadActualizada)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long id) {
        return actividadService.eliminarActividad(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}