package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services.MedioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/medios")
@RequiredArgsConstructor
public class MedioController {

    private final MedioService medioService;

    @PostMapping
    public ResponseEntity<?> crearMedio(@Valid @RequestBody MedioDTO medioDTO) {
        MedioDTO nuevoMedio = medioService.crearMedio(medioDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Medio creado exitosamente",
                        "medio", nuevoMedio
                ));
    }

    @GetMapping
    public ResponseEntity<List<MedioDTO>> obtenerTodos() {
        return ResponseEntity.ok(medioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedio(
            @PathVariable Long id,
            @Valid @RequestBody MedioDTO medioDTO) {

        MedioDTO actualizado = medioService.actualizarMedio(id, medioDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Medio actualizado exitosamente",
                "medio", actualizado
              )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedio(@PathVariable Long id) {
        medioService.eliminarMedio(id);
       
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Medio eliminado exitosamente"
                )
        );
    }

    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<List<MedioDTO>> obtenerMediosPorArbolDeObjetivosId(
            @PathVariable Long arbolDeObjetivosId) {

        return ResponseEntity.ok(
                medioService.obtenerMediosPorArbolDeObjetivosId(arbolDeObjetivosId));
    }

}