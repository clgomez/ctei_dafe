package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Controller;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services.CausaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/causa")
@RequiredArgsConstructor
public class CausaController {

    private final CausaService causaService;

    @PostMapping
    public ResponseEntity<?> crearCausa(@Valid @RequestBody CausaDTO causaDTO) {
        CausaDTO nuevaCausa = causaService.crearCausa(causaDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Causa creada exitosamente",
                        "causa", nuevaCausa
                ));
    }

    @GetMapping
    public ResponseEntity<List<CausaDTO>> obtenerTodos() {
        return ResponseEntity.ok(causaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CausaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(causaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCausa(
            @PathVariable Long id,
            @Valid @RequestBody CausaDTO causaDTO) {

        CausaDTO actualizada = causaService.actualizarCausa(id, causaDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Causa actualizada exitosamente",
                "causa", actualizada
              )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCausa(@PathVariable Long id) {
        causaService.eliminarCausa(id);
        
         return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Causa eliminada exitosamente"
                )
        );
    }

    @GetMapping("/arbol/{arbolDeProblemasId}")
    public ResponseEntity<List<CausaDTO>> obtenerCausasPorArbolDeProblemasId(
            @PathVariable Long arbolDeProblemasId) {

        return ResponseEntity.ok(
                causaService.obtenerCausasPorArbolDeProblemasId(arbolDeProblemasId));
    }

  }