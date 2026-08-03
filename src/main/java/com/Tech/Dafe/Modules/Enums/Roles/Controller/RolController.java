package com.Tech.Dafe.Modules.Enums.Roles.Controller;

import com.Tech.Dafe.Modules.Enums.Roles.DTO.RolDTO;
import com.Tech.Dafe.Modules.Enums.Roles.Service.RolService;
import com.Tech.Dafe.Response.Mensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;
    
    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody RolDTO rol) {
        try {
            RolDTO nuevoRol = rolService.crearRol(rol);
            return ResponseEntity.ok(nuevoRol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerRol(@PathVariable Long id) {
        return rolService.obtenerRol(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRol(@PathVariable Long id, @RequestBody RolDTO rol) {
        try {
            RolDTO rolActualizado = rolService.actualizarRol(id, rol);
            return rolActualizado != null
                    ? ResponseEntity.ok(rolActualizado)
                    : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new Mensaje(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RolDTO>> listarRoles() {
        List<RolDTO> roles = rolService.listarRoles();
        return ResponseEntity.ok(roles);
    }



}
