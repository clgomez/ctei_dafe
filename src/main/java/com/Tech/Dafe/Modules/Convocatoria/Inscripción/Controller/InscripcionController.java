package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Controller;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service.InscripcionService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        List<Inscripcion> inscripciones = new ArrayList<>();
        try {
               inscripciones = inscripcionService.obtenerTodasInscripciones();
            return ResponseEntity.ok(inscripciones);
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(new Mensaje(e.getMessage()));
        }
  
    }


  /*   @GetMapping
    public ResponseEntity<List<Inscripcion>> obtenerInscripciones() {
        List<Inscripcion> inscripciones = inscripcionService.obtenerTodasInscripciones();
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }
*/

    @GetMapping("/{id}")
    public ResponseEntity<?> ver(@PathVariable Long id) {
        try {
            Optional<Inscripcion> o = inscripcionService.obtenerInscripcion(id);
            InscripcionProyectoDTO inscripcionProyectoDTO = new InscripcionProyectoDTO(null,null,null);
            inscripcionProyectoDTO.setProyectoId(o.get().getProyecto().getId());
            inscripcionProyectoDTO.setConvocatoriaId(o.get().getConvocatoria().getId());
            inscripcionProyectoDTO.setEstado(o.get().getEstado());
       
            System.out.println("id Proyecto: "+ inscripcionProyectoDTO.getProyectoId());
            System.out.println("id Convocatoria: "+ inscripcionProyectoDTO.getConvocatoriaId());
            System.out.println("Estado: "+ inscripcionProyectoDTO.getEstado());
            if (o.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Elemento no encontrado con ID: " + id);
            }
            return ResponseEntity.ok().body(inscripcionProyectoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el elemento: " + e.getMessage());
        }
    }



}
