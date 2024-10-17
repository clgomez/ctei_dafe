package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Controller;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service.InscripcionService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;


    @PostMapping
    public ResponseEntity<?> inscribirProyecto(@RequestBody InscripcionProyectoDTO dto) {
        try {
            Inscripcion nuevaInscripcion = inscripcionService.inscribirProyecto(dto);
            return new ResponseEntity<>(nuevaInscripcion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Mensaje mensajeError = new Mensaje(e.getMessage());
            return ResponseEntity.badRequest().body(mensajeError);
        }
    }

    @PutMapping("/{inscripcionId}")
    public ResponseEntity<?> actualizarInscripcion(@PathVariable Long inscripcionId, @RequestBody InscripcionProyectoDTO dto) {
        try {
            Inscripcion actualizadaInscripcion = inscripcionService.actualizarInscripcion(inscripcionId, dto);
            return ResponseEntity.ok(actualizadaInscripcion);
        } catch (IllegalArgumentException e) {
            Mensaje mensajeError = new Mensaje(e.getMessage());
            return ResponseEntity.badRequest().body(mensajeError);
        }
    }

    @DeleteMapping("/{inscripcionId}")
    public ResponseEntity<?> eliminarInscripcion(@PathVariable Long inscripcionId) {
        try {
            inscripcionService.eliminarInscripcion(inscripcionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerInscripciones() {
        try {
            List<Inscripcion> inscripciones = inscripcionService.obtenerTodasInscripciones();
            return ResponseEntity.ok(inscripciones);
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(new Mensaje(e.getMessage()));
        }
    }


}
