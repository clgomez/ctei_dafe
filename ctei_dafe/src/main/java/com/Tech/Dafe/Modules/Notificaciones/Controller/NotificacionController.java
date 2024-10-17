package com.Tech.Dafe.Modules.Notificaciones.Controller;

import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import com.Tech.Dafe.Response.Mensaje;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No se encontró el usuario con ID: " + usuarioId));
        }

        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuarioId);

        if (notificaciones.isEmpty()) {
            return ResponseEntity.ok(new Mensaje("No hay notificaciones para el usuario con ID: " + usuarioId));
        }

        return ResponseEntity.ok(notificaciones);
    }


    @DeleteMapping("/{notificacionId}")
    public ResponseEntity<Mensaje> eliminarNotificacion(@PathVariable Long notificacionId) {
        try {
            notificacionService.eliminarNotificacion(notificacionId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No se encontró la notificación con ID: " + notificacionId));
        }
    }

    @PutMapping("/{notificacionId}/leida")
    public ResponseEntity<Mensaje> marcarComoLeida(@PathVariable Long notificacionId) {
        try {
            notificacionService.marcarComoLeida(notificacionId);
            return ResponseEntity.ok( new Mensaje("Notificación marcada como leida"));
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No se encontró la notificación con ID: " + notificacionId));
        }
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodasNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.obtenerTodasNotificaciones();
        return ResponseEntity.ok(notificaciones);
    }
}
