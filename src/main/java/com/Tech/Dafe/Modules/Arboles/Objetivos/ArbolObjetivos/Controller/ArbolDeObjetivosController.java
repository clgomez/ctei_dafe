package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services.ArbolDeObjetivosService;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arboles-objetivos")
public class ArbolDeObjetivosController {

    @Autowired
    private ArbolDeObjetivosService arbolDeObjetivosService;

    @PostMapping
    public ResponseEntity<?> crearArbolDeObjetivos(@RequestBody ArbolDeObjetivosDTO arbolDTO) {
        try {
            ArbolDeObjetivosDTO nuevoArbol = arbolDeObjetivosService.crearArbolDeObjetivos(arbolDTO);
            return ResponseEntity.ok(nuevoArbol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ArbolDeObjetivosDTO>> obtenerTodos() {
        List<ArbolDeObjetivosDTO> arboles = arbolDeObjetivosService.obtenerTodos();
        return ResponseEntity.ok(arboles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArbolDeObjetivosDTO> obtenerPorId(@PathVariable Long id) {
        return arbolDeObjetivosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArbolDeObjetivosDTO> actualizarArbolDeObjetivos(@PathVariable Long id, @RequestBody ArbolDeObjetivosDTO arbolDTO) {
        ArbolDeObjetivosDTO arbolActualizado = arbolDeObjetivosService.actualizarArbolDeObjetivos(id, arbolDTO);
        return arbolActualizado != null
                ? ResponseEntity.ok(arbolActualizado)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArbolDeObjetivos(@PathVariable Long id) {
        return arbolDeObjetivosService.eliminarArbolDeObjetivos(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<ArbolDeObjetivosDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody ArbolDeObjetivosDTO arbolDTO) {
        ArbolDeObjetivosDTO arbolActualizado = arbolDeObjetivosService.actualizarCalificacion(id, arbolDTO);
        return arbolActualizado != null
                ? ResponseEntity.ok(arbolActualizado)
                : ResponseEntity.notFound().build();
    }



}
