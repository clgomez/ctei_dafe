package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Controller;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO.ConvocatoriaDTO;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Service.ConvocatoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/convocatorias")
public class ConvocatoriaController {

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @PostMapping
    public ResponseEntity<Convocatoria> crearConvocatoria(@RequestBody ConvocatoriaDTO convocatoriaDTO) {
        Convocatoria convocatoria = convocatoriaService.crearConvocatoria(convocatoriaDTO);
        return new ResponseEntity<>(convocatoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Convocatoria>> obtenerConvocatorias() {
        List<Convocatoria> convocatorias = convocatoriaService.obtenerConvocatorias();
        return new ResponseEntity<>(convocatorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convocatoria> obtenerConvocatoriaPorId(@PathVariable Long id) {
        Convocatoria convocatoria = convocatoriaService.obtenerConvocatoriaPorId(id);
        return convocatoria != null ? new ResponseEntity<>(convocatoria, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Convocatoria> actualizarConvocatoria(@PathVariable Long id, @RequestBody ConvocatoriaDTO convocatoriaDTO) {
        Convocatoria convocatoria = convocatoriaService.actualizarConvocatoria(id, convocatoriaDTO);
        return convocatoria != null ? new ResponseEntity<>(convocatoria, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConvocatoria(@PathVariable Long id) {
        convocatoriaService.eliminarConvocatoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
