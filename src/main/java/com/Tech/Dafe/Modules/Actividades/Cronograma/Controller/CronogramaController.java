package com.Tech.Dafe.Modules.Actividades.Cronograma.Controller;

import com.Tech.Dafe.Modules.Actividades.Cronograma.DTO.CronogramaDTO;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Service.CronogramaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cronogramas")
@RequiredArgsConstructor
public class CronogramaController {

    private final CronogramaService cronogramaService;

    @PostMapping
    public ResponseEntity<?> crearCronograma(@Valid @RequestBody CronogramaDTO cronogramaDTO) {
        
        CronogramaDTO cronograma = cronogramaService.crearCronograma(cronogramaDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Cronograma creado exitosamente",
                        "cronograma", cronograma
                ));
    }

    @GetMapping
    public ResponseEntity<List<CronogramaDTO>> obtenerTodos() {
        return ResponseEntity.ok(cronogramaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CronogramaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cronogramaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCronograma(
            @PathVariable Long id,
            @Valid @RequestBody CronogramaDTO cronogramaDTO) {

        CronogramaDTO actualizado = cronogramaService.actualizarCronograma(id, cronogramaDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Cronograma actualizado exitosamente",
                "cronograma", actualizado
              )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCronograma(@PathVariable Long id) {
        cronogramaService.eliminarCronograma(id);
        
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Cronograma eliminado exitosamente"
                )
        );


    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<CronogramaDTO> obtenerCronogramaPorIdActividad(
            @PathVariable Long actividadId) {

        return ResponseEntity.ok(
                cronogramaService.obtenerCronogramaPorIdActividad(actividadId));
    }
}