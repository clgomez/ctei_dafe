package com.Tech.Dafe.Modules.Calificaciones.Controller;

import com.Tech.Dafe.Modules.Calificaciones.Service.CalificacionesService;
import com.Tech.Dafe.Modules.Calificaciones.DTO.CalificacionesDTO;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calificaciones")
public class CalificacionesController {

    @Autowired
    private CalificacionesService calificacionesService;

    @PostMapping
    public ResponseEntity<?> crearCalificacion(@RequestBody CalificacionesDTO calificacionDTO) {
        try {
            Calificacion calificacionCreada = calificacionesService.crearCalificacion(calificacionDTO);
            return ResponseEntity.ok(calificacionCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Error interno del servidor: " + e.getMessage()));
        }
    }


    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Calificacion>> obtenerCalificacionesPorProyecto(@PathVariable Long proyectoId) {
        List<Calificacion> calificaciones = calificacionesService.obtenerCalificacionesPorProyecto(proyectoId);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/{calificacionId}")
    public ResponseEntity<?> obtenerCalificacionPorId(@PathVariable Long calificacionId) {
        try {
            Calificacion calificacion = calificacionesService.obtenerCalificacionPorId(calificacionId);
            return ResponseEntity.ok(calificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Error interno del servidor: " + e.getMessage()));
        }
    }


    @PutMapping("/{calificacionId}")
    public ResponseEntity<?> actualizarCalificacion(@PathVariable Long calificacionId, @RequestBody CalificacionesDTO calificacionDTO) {
        try {
            Calificacion calificacionActualizada = calificacionesService.actualizarCalificacion(calificacionId, calificacionDTO);
            return ResponseEntity.ok(calificacionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Error interno del servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{calificacionId}")
    public ResponseEntity<?> eliminarCalificacion(@PathVariable Long calificacionId) {
        try {
            calificacionesService.eliminarCalificacion(calificacionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Error interno del servidor: " + e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<List<Calificacion>> obtenerTodasCalificaciones() {
        List<Calificacion> calificaciones = calificacionesService.obtenerTodasCalificaciones();
        return ResponseEntity.ok(calificaciones);
    }
}
