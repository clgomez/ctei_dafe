package com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Controller;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.DTO.FinesDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Services.FinesService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FinesController {

    @Autowired
    private FinesService finesService;

    @PostMapping
    public ResponseEntity<?> crearFines(@RequestBody FinesDTO finesDTO) {
        try {
            FinesDTO nuevoFine = finesService.crearFines(finesDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoFine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<FinesDTO>> obtenerTodos() {
        List<FinesDTO> fines = finesService.obtenerTodos();
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            FinesDTO fine = finesService.obtenerPorId(id);
            return ResponseEntity.ok(fine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedio(@PathVariable Long id, @RequestBody FinesDTO finesDTO) {
        try {
            FinesDTO fineActualizado = finesService.actualizarMedio(id, finesDTO);
            return ResponseEntity.ok(fineActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedio(@PathVariable Long id) {
        try {
            finesService.eliminarMedio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<?> obtenerPorArbolDeObjetivosId(@PathVariable Long arbolDeObjetivosId) {
        try {
            List<FinesDTO> fines = finesService.obtenerPorArbolDeObjetivosId(arbolDeObjetivosId);
            return ResponseEntity.ok(fines);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    //CALIFICACION

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<FinesDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody FinesDTO finesDTO) {
        FinesDTO finesActualizado = finesService.actualizarCalificacion(id, finesDTO);
        return finesActualizado != null
                ? ResponseEntity.ok(finesActualizado)
                : ResponseEntity.notFound().build();
    }


}
