package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Controller;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolDeProblemasDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services.ArbolDeProblemasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/arboles-problemas")
@RequiredArgsConstructor
public class ArbolDeProblemasController {

    private final ArbolDeProblemasService arbolDeProblemasService;

    @PostMapping
    public ResponseEntity<?> crearArbolDeProblemas(
            @Valid @RequestBody ArbolDeProblemasDTO arbolDTO) {

       
        ArbolDeProblemasDTO  arbolDeProblemasDTO = arbolDeProblemasService.crearArbolDeProblemas(
                        arbolDTO,
                        arbolDTO.getProyectoId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Árbol de problemas creado exitosamente",
                        "arbolDeProblemas", arbolDeProblemasDTO
                ));
    }

    @GetMapping
    public ResponseEntity<List<ArbolDeProblemasDTO>> obtenerTodos() {
        return ResponseEntity.ok(arbolDeProblemasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArbolDeProblemasDTO> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(arbolDeProblemasService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarArbolDeProblemas(
            @PathVariable Long id,
            @Valid @RequestBody ArbolDeProblemasDTO arbolDTO) {

       
        ArbolDeProblemasDTO actualizado = arbolDeProblemasService.actualizarArbolDeProblemas(id, arbolDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Árbol de problemas actualizado exitosamente",
                "arbolDeProblemas", actualizado
              )
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArbolDeProblemas(
            @PathVariable Long id) {

        arbolDeProblemasService.eliminarArbolDeProblemas(id);
 
        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Árbol de problemas eliminado exitosamente"
                )
        );
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<ArbolDeProblemasDTO> obtenerArbolDeProblemasPorIdProyecto(
            @PathVariable Long proyectoId) {

        return ResponseEntity.ok(
                arbolDeProblemasService.obtenerArbolDeProblemasPorIdProyecto(proyectoId));
    }

}