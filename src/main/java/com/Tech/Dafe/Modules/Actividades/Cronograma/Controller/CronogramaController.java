package com.Tech.Dafe.Modules.Actividades.Cronograma.Controller;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Service.CronogramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cronogramas")
public class CronogramaController {

    @Autowired
    private CronogramaService cronogramaService;

    @PostMapping
    public ResponseEntity<Cronograma> crearCronograma(@RequestBody Cronograma cronograma) {
        Cronograma nuevoCronograma = cronogramaService.crearCronograma(cronograma);
        return ResponseEntity.ok(nuevoCronograma);
    }

    @GetMapping
    public ResponseEntity<List<Cronograma>> obtenerTodos() {
        List<Cronograma> cronogramas = cronogramaService.obtenerTodos();
        return ResponseEntity.ok(cronogramas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cronograma> obtenerPorId(@PathVariable Long id) {
        return cronogramaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cronograma> actualizarCronograma(@PathVariable Long id, @RequestBody Cronograma cronograma) {
        Cronograma cronogramaActualizado = cronogramaService.actualizarCronograma(id, cronograma);
        return cronogramaActualizado != null
                ? ResponseEntity.ok(cronogramaActualizado)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCronograma(@PathVariable Long id) {
        return cronogramaService.eliminarCronograma(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
