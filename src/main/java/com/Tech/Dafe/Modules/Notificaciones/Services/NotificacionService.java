package com.Tech.Dafe.Modules.Notificaciones.Services;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import java.util.List;

public interface NotificacionService {


    void enviarNotificacionesAUsuario(Long usuarioId, String mensaje);

    List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId);

    void enviarNotificacionPorCalificacion(Long usuarioId, String mensaje, Long calificacionId);

    void enviarNotificacionPorCalificacionYInscripcion(Long usuarioId, String mensaje, Long calificacionId, Long inscripcionId);


    void enviarNotificacionesPorRol(RolNombre rolNombre, String mensaje);

    void eliminarNotificacion(Long notificacionId);

    void marcarComoLeida(Long notificacionId);

    List<Notificacion> obtenerTodasNotificaciones();
}
