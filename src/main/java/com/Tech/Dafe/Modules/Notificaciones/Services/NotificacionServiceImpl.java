package com.Tech.Dafe.Modules.Notificaciones.Services;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Calificaciones.Repository.CalificacionRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository.InscripcionRepository;
import com.Tech.Dafe.Modules.Enums.Notificaciones.Enums.EstadoNotificacion;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Notificaciones.Repository.NotificacionRepository;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final UsuarioRepository usuarioRepository;
    
    private final NotificacionRepository notificacionRepository;

    private final CalificacionRepository calificacionesRepository;
    
    private final InscripcionRepository inscripcionRepository;


    public void enviarNotificacionesPorRol(RolNombre rolNombre, String mensaje) {
        List<Usuario> usuarios = usuarioRepository.findByRoles_RolNombre(rolNombre);

        for (Usuario usuario : usuarios) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
            notificacion.setFechaNotificacion(LocalDateTime.now());
            notificacion.setUsuario(usuario);
            notificacion.setLeida(false);
            notificacionRepository.save(notificacion);
        }
    }


    public void enviarNotificacionesAUsuario(Long usuarioId, String mensaje) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "No se encontró el usuario con ID: " + usuarioId));
        
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
            notificacion.setFechaNotificacion(LocalDateTime.now());
            notificacion.setUsuario(usuario);
            notificacion.setLeida(false);
            notificacionRepository.save(notificacion);
        
    }

    public void enviarNotificacionPorCalificacion(Long usuarioId, String mensaje, Long calificacionId) {
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "No se encontró el usuario con ID: " + usuarioId));
        
        Calificacion calificacion = calificacionesRepository.findById(calificacionId)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "No se encontró la calificación con ID: " + calificacionId));

            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
            notificacion.setFechaNotificacion(LocalDateTime.now());
            notificacion.setUsuario(usuario);
            notificacion.setLeida(false);
            notificacion.setCalificacion(calificacion);

            notificacionRepository.save(notificacion);
        
    }

    @Override
    public void enviarNotificacionPorCalificacionYInscripcion(
            Long usuarioId,
            String mensaje,
            Long calificacionId,
            Long inscripcionId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el usuario con ID: " + usuarioId));

        Calificacion calificacion = calificacionesRepository.findById(calificacionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la calificación con ID: " + calificacionId));

        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la inscripción con ID: " + inscripcionId));

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
        notificacion.setFechaNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        notificacion.setLeida(false);
        notificacion.setCalificacion(calificacion);
        notificacion.setInscripcion(inscripcion);

        notificacionRepository.save(notificacion);
    }


    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el usuario con ID: " + usuarioId);
        }

        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public void eliminarNotificacion(Long notificacionId) {

        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la notificación con ID: " + notificacionId));

        notificacionRepository.delete(notificacion);
    }


    @Override
    public Notificacion marcarComoLeida(Long notificacionId) {

        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la notificación con ID: " + notificacionId));

        notificacion.setLeida(true);
        notificacion.setEstado(EstadoNotificacion.LEIDA);

        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerTodasNotificaciones() {
        return notificacionRepository.findAll();
    }
}
