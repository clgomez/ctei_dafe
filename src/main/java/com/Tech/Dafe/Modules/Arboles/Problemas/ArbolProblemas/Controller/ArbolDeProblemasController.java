package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Controller;


import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolProblemasDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services.ArbolDeProblemasServices;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arboles-problemas")
public class ArbolDeProblemasController {

    @Autowired
    private ArbolDeProblemasServices arbolDeProblemasService;

    @PostMapping
    public ResponseEntity<?> crearArbolDeProblemas(@RequestBody ArbolProblemasDTO arbolDTO) {
        try {
            Long proyectoId = arbolDTO.getProyectoId();
            ArbolProblemasDTO nuevoArbol = arbolDeProblemasService.crearArbolDeProblemas(arbolDTO, proyectoId);
            return ResponseEntity.ok(nuevoArbol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ArbolProblemasDTO>> obtenerTodos() {
        List<ArbolProblemasDTO> arboles = arbolDeProblemasService.obtenerTodos();
        return ResponseEntity.ok(arboles);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArbolProblemasDTO> obtenerPorId(@PathVariable Long id) {
        ArbolProblemasDTO arbol = arbolDeProblemasService.obtenerPorId(id);
        return arbol != null ? ResponseEntity.ok(arbol) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArbolProblemasDTO> actualizarArbolDeProblemas(
            @PathVariable Long id,
            @RequestBody ArbolProblemasDTO arbolDTO) {
        ArbolProblemasDTO arbolActualizado = arbolDeProblemasService.actualizarArbolDeProblemas(id, arbolDTO);
        return arbolActualizado != null
                ? ResponseEntity.ok(arbolActualizado)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArbolDeProblemas(@PathVariable Long id) {
        boolean eliminado = arbolDeProblemasService.eliminarArbolDeProblemas(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    //CALIFICACION

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<ArbolProblemasDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody ArbolProblemasDTO arbolDTO) {
        ArbolProblemasDTO arbolActualizado = arbolDeProblemasService.actualizarCalificacion(id, arbolDTO);
        return arbolActualizado != null
                ? ResponseEntity.ok(arbolActualizado)
                : ResponseEntity.notFound().build();
    }


}
