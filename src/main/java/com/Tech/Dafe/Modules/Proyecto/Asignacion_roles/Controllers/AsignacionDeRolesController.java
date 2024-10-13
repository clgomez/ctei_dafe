package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Controllers;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services.AsignacionDeRolesService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignacion-roles")
public class AsignacionDeRolesController {

    @Autowired
    private AsignacionDeRolesService asignacionDeRolesService;

    @PostMapping
    public ResponseEntity<?> crearAsignacion(@RequestBody AsignacionDeRolesDTO asignacion) {
        try {
            AsignacionDeRolesDTO nuevaAsignacion = asignacionDeRolesService.crearAsignacion(asignacion);
            return ResponseEntity.ok(nuevaAsignacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDeRolesDTO> obtenerAsignacion(@PathVariable Long id) {
        return asignacionDeRolesService.obtenerAsignacion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAsignacion(@PathVariable Long id, @RequestBody AsignacionDeRolesDTO asignacion) {
        try {
            AsignacionDeRolesDTO asignacionActualizada = asignacionDeRolesService.actualizarAsignacion(id, asignacion);
            return asignacionActualizada != null
                    ? ResponseEntity.ok(asignacionActualizada)
                    : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long id) {
        asignacionDeRolesService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AsignacionDeRolesDTO>> listarAsignaciones() {
        List<AsignacionDeRolesDTO> asignaciones = asignacionDeRolesService.listarAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }
}
