package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services.ArbolDeObjetivosService;

import jakarta.validation.Valid;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/arboles-objetivos")
@RequiredArgsConstructor
public class ArbolDeObjetivosController {

    private final ArbolDeObjetivosService arbolDeObjetivosService;

    @PostMapping
    public ResponseEntity<?> crearArbolDeObjetivos(
            @Valid @RequestBody ArbolDeObjetivosDTO arbolDTO) {

        ArbolDeObjetivosDTO arbolDeObjetivosDTO = arbolDeObjetivosService.crearArbolDeObjetivos(arbolDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Árbol de objetivos creado exitosamente",
                "arbolDeObjetivos", arbolDeObjetivosDTO
        ));
    }

    @GetMapping
    public ResponseEntity<List<ArbolDeObjetivosDTO>> obtenerTodos() {
        return ResponseEntity.ok(arbolDeObjetivosService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArbolDeObjetivosDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(arbolDeObjetivosService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarArbolDeObjetivos(
            @PathVariable Long id,
            @Valid @RequestBody ArbolDeObjetivosDTO arbolDTO) {

        ArbolDeObjetivosDTO actualizado =
                arbolDeObjetivosService.actualizarArbolDeObjetivos(id, arbolDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Árbol de objetivos actualizado exitosamente",
                "arbolDeObjetivos", actualizado
              )
        );        

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArbolDeObjetivos(@PathVariable Long id) {

        arbolDeObjetivosService.eliminarArbolDeObjetivos(id);

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Árbol de objetivos eliminado exitosamente"
                )
        );
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<ArbolDeObjetivosDTO> obtenerArbolDeObjetivosPorIdProyecto(
            @PathVariable Long proyectoId) {

        return ResponseEntity.ok(
                arbolDeObjetivosService.obtenerArbolDeObjetivosPorIdProyecto(proyectoId));
    }

}
