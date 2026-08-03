package com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.DTO.FinDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Services.FinService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FinController {

    private final FinService finService;

    @PostMapping
    public ResponseEntity<?> crearFin(@Valid @RequestBody FinDTO finDTO) {
        FinDTO nuevoFin = finService.crearFin(finDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Fin creado exitosamente",
                        "fin", nuevoFin
                ));
    }

    @GetMapping
    public ResponseEntity<List<FinDTO>> obtenerTodos() {
        return ResponseEntity.ok(finService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(finService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarFin(
            @PathVariable Long id,
            @Valid @RequestBody FinDTO finDTO) {

        FinDTO actualizado = finService.actualizarFin(id, finDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Fin actualizado exitosamente",
                "fin", actualizado
              )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFin(@PathVariable Long id) {
        finService.eliminarFin(id);
       
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Fin eliminado exitosamente"
                )
        );
    }

    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<List<FinDTO>> obtenerFinesPorArbolDeObjetivosId(
            @PathVariable Long arbolDeObjetivosId) {

        return ResponseEntity.ok(
                finService.obtenerFinesPorArbolDeObjetivosId(arbolDeObjetivosId));
    }

}