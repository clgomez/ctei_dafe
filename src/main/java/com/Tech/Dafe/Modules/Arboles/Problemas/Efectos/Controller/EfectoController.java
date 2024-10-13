package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Controller;

import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services.EfectoService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/efecto")
public class EfectoController {

    @Autowired
    private EfectoService efectoService;

    @PostMapping
    public ResponseEntity<?> crearFines(@RequestBody EfectoDTO finesDTO) {
        try {
            EfectoDTO nuevoEfecto = efectoService.crearEfecto(finesDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEfecto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<EfectoDTO>> obtenerTodos() {
        List<EfectoDTO> efecto = efectoService.obtenerTodos();
        return ResponseEntity.ok(efecto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            EfectoDTO efectoDTO = efectoService.obtenerPorId(id);
            return ResponseEntity.ok(efectoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedio(@PathVariable Long id, @RequestBody EfectoDTO efectoDTO) {
        try {
            EfectoDTO efectoActualizado = efectoService.actualizarMedio(id, efectoDTO);
            return ResponseEntity.ok(efectoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedio(@PathVariable Long id) {
        try {
            efectoService.eliminarMedio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<?> obtenerPorArbolDeObjetivosId(@PathVariable Long arbolDeObjetivosId) {
        try {
            List<EfectoDTO> efecto = efectoService.obtenerPorArbolDeObjetivosId(arbolDeObjetivosId);
            return ResponseEntity.ok(efecto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    //CALIFICACION

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<EfectoDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody EfectoDTO efectoDTO) {
        EfectoDTO efectoActualizado = efectoService.actualizarCalificacion(id, efectoDTO);
        return efectoActualizado != null
                ? ResponseEntity.ok(efectoActualizado)
                : ResponseEntity.notFound().build();
    }



}
