package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Controller;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO.ConvocatoriaDTO;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Service.ConvocatoriaService;
import com.Tech.Dafe.Utils.NormalizarTitulo.ValidarTituloDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/convocatorias")
@RequiredArgsConstructor
public class ConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    @PostMapping
    public ResponseEntity<?> crearConvocatoria(
            @Valid @RequestBody ConvocatoriaDTO convocatoriaDTO) {

        Convocatoria convocatoria = convocatoriaService.crearConvocatoria(convocatoriaDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "codigo", "SUCCESS",
                                "mensaje", "Convocatoria creada exitosamente",
                                "convocatoria", convocatoria
                        ));

    }

    @GetMapping
    public ResponseEntity<List<Convocatoria>> obtenerConvocatorias() {
        return ResponseEntity.ok(convocatoriaService.obtenerConvocatorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convocatoria> obtenerConvocatoriaPorId(@PathVariable Long id) {

        return ResponseEntity.ok(convocatoriaService.obtenerConvocatoriaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarConvocatoria(
            @PathVariable Long id,
            @Valid @RequestBody ConvocatoriaDTO convocatoriaDTO) {

        Convocatoria actualizada = 
                convocatoriaService.actualizarConvocatoria(id, convocatoriaDTO);

         return ResponseEntity.ok(
            Map.of(
                    "codigo", "SUCCESS",
                    "mensaje", "Convocatoria actualizada exitosamente",
                    "convocatoria", actualizada
            )    
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarConvocatoria(@PathVariable Long id) {

        convocatoriaService.eliminarConvocatoria(id);

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Convocatoria eliminada exitosamente"
                )
        );

    }

    @GetMapping("/proyecto/{proyectoId}/usuarioinvestigador/{usuarioInvestigadorId}")
    public ResponseEntity<Convocatoria> obtenerConvocatoriaPorProyectoIdYUsuarioInvestigadorId(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioInvestigadorId) {

        Convocatoria convocatoria = convocatoriaService
                .obtenerConvocatoriaPorProyectoIdYUsuarioInvestigadorId(
                        proyectoId,
                        usuarioInvestigadorId)
                .orElseThrow();

        return ResponseEntity.ok(convocatoria);
    }

    @PostMapping("/validar-titulo-convocatoria")
    public ResponseEntity<?> validarTitulo(
                @Valid @RequestBody ValidarTituloDTO dto) {

        convocatoriaService.validarTituloConvocatoria(
                dto.getTitulo(),
                dto.getId()
        );

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "No existe ninguna convocatoria con ese título"
              )
        );
    }
}
