package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Controller;

import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services.EfectoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/efecto")
@RequiredArgsConstructor
public class EfectoController {

    private final EfectoService efectoService;

    @PostMapping
    public ResponseEntity<?> crearEfecto(@Valid @RequestBody EfectoDTO efectoDTO) {
        EfectoDTO nuevoEfecto = efectoService.crearEfecto(efectoDTO);
        
         return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Efecto creado exitosamente",
                        "efecto", nuevoEfecto
                ));
    }

    @GetMapping
    public ResponseEntity<List<EfectoDTO>> obtenerTodos() {
        return ResponseEntity.ok(efectoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EfectoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(efectoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEfecto(
            @PathVariable Long id,
            @Valid @RequestBody EfectoDTO efectoDTO) {

        EfectoDTO actualizado = efectoService.actualizarEfecto(id, efectoDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Efecto actualizado exitosamente",
                "efecto", actualizado
              )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEfecto(@PathVariable Long id) {
        efectoService.eliminarEfecto(id);
        
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Efecto eliminado exitosamente"
                )
        );
    }

    @GetMapping("/arbol/{arbolDeProblemasId}")
    public ResponseEntity<List<EfectoDTO>> obtenerEfectosPorArbolDeProblemasId(
            @PathVariable Long arbolDeProblemasId) {

        return ResponseEntity.ok(
                efectoService.obtenerEfectosPorArbolDeProblemasId(arbolDeProblemasId)
        );
    }

}