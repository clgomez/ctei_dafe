package com.Tech.Dafe.Modules.Notificaciones.Controller;

import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                notificacionService.obtenerNotificacionesPorUsuario(usuarioId)
        );
    }

    @DeleteMapping("/{notificacionId}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Long notificacionId) {
        notificacionService.eliminarNotificacion(notificacionId);

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Notificacion eliminada exitosamente"
                )
        );
    }

    @PutMapping("/{notificacionId}/leida")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long notificacionId) {

        Notificacion actualizada = notificacionService.marcarComoLeida(notificacionId);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Notificación marcada como leída",
                "notificacion", actualizada
              )
        );

    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodasNotificaciones() {
        return ResponseEntity.ok(
                notificacionService.obtenerTodasNotificaciones()
        );
    }

}