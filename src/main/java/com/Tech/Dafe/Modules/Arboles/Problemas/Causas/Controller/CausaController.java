package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Controller;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services.CausaService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/causa")
public class CausaController {

    @Autowired
    private CausaService causaService;

    @PostMapping
    public ResponseEntity<?> crearFines(@RequestBody CausaDTO finesDTO) {
        try {
            CausaDTO nuevaCausa = causaService.crearCausa(finesDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCausa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CausaDTO>> obtenerTodos() {
        List<CausaDTO> causa = causaService.obtenerTodos();
        return ResponseEntity.ok(causa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            CausaDTO causaDTO = causaService.obtenerPorId(id);
            return ResponseEntity.ok(causaDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedio(@PathVariable Long id, @RequestBody CausaDTO causaDTO) {
        try {
            CausaDTO fineActualizado = causaService.actualizarMedio(id, causaDTO);
            return ResponseEntity.ok(fineActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedio(@PathVariable Long id) {
        try {
            causaService.eliminarMedio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    @GetMapping("/arbol/{arbolDeObjetivosId}")
    public ResponseEntity<?> obtenerPorArbolDeObjetivosId(@PathVariable Long arbolDeObjetivosId) {
        try {
            List<CausaDTO> causa = causaService.obtenerPorArbolDeObjetivosId(arbolDeObjetivosId);
            return ResponseEntity.ok(causa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }


    //CALIFICACION

    @PutMapping("/{id}/calificacion")
    public ResponseEntity<CausaDTO> actualizarCalificacion(
            @PathVariable Long id,
            @RequestBody CausaDTO causaDTO) {
        CausaDTO causaActualizado = causaService.actualizarCalificacion(id, causaDTO);
        return causaActualizado != null
                ? ResponseEntity.ok(causaActualizado)
                : ResponseEntity.notFound().build();
    }



}
