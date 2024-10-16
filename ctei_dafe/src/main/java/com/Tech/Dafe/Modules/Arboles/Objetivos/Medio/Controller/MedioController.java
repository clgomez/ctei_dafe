package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services.MedioService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medios")
public class MedioController {

    @Autowired
    private MedioService medioService;

    @PostMapping
    public ResponseEntity<?> crearMedio(@RequestBody MedioDTO medioDTO) {
        try {
            MedioDTO nuevoMedio = medioService.crearMedio(medioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMedio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<MedioDTO>> obtenerTodos() {
        List<MedioDTO> medios = medioService.obtenerTodos();
        return ResponseEntity.ok(medios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            MedioDTO medio = medioService.obtenerPorId(id);
            return ResponseEntity.ok(medio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedio(@PathVariable Long id, @RequestBody MedioDTO medioDTO) {
        try {
            MedioDTO medioActualizado = medioService.actualizarMedio(id, medioDTO);
            return ResponseEntity.ok(medioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedio(@PathVariable Long id) {
        try {
            medioService.eliminarMedio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<?> obtenerPorArbolDeObjetivosId(@PathVariable Long arbolDeObjetivosId) {
        try {
            List<MedioDTO> medios = medioService.obtenerPorArbolDeObjetivosId(arbolDeObjetivosId);
            return ResponseEntity.ok(medios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    //CALIFICACION

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<MedioDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody MedioDTO medioDTO) {
        MedioDTO medioActualizado = medioService.actualizarCalificacion(id, medioDTO);
        return medioActualizado != null
                ? ResponseEntity.ok(medioActualizado)
                : ResponseEntity.notFound().build();
    }


}
