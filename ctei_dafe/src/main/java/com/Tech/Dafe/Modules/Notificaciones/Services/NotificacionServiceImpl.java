package com.Tech.Dafe.Modules.Notificaciones.Services;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Calificaciones.Repository.CalificacionesRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository.InscripcionRepository;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Notificaciones.Repository.NotificacionesRepository;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionesRepository notificacionRepository;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    @Autowired
    private CalificacionesRepository calificacionesRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;




    public void enviarNotificacionesPorRol(RolNombre rolNombre, String mensaje) {
        List<Usuario> usuarios = usuarioRepository.findByRoles_RolNombre(rolNombre);

        for (Usuario usuario : usuarios) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado("ENVIADA");
            notificacion.setFechaNotificacion(java.time.LocalDateTime.now().toString());
            notificacion.setUsuario(usuario);
            notificacion.setLeida(false);
            notificacionRepository.save(notificacion);
        }
    }


    public void enviarNotificacionesAUsuario(Long usuarioId, String mensaje) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado("ENVIADA");
            notificacion.setFechaNotificacion(java.time.LocalDateTime.now().toString());
            notificacion.setUsuario(usuarioOpt.get());
            notificacion.setLeida(false);
            notificacionesRepository.save(notificacion);
        }
    }

    public void enviarNotificacionPorCalificacion(Long usuarioId, String mensaje, Long calificacionId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Calificacion> calificacionOpt = calificacionesRepository.findById(calificacionId);

        if (usuarioOpt.isPresent() && calificacionOpt.isPresent()) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado("ENVIADA");
            notificacion.setFechaNotificacion(java.time.LocalDateTime.now().toString());
            notificacion.setUsuario(usuarioOpt.get());
            notificacion.setLeida(false);
            notificacion.setCalificacion(calificacionOpt.get());

            notificacionRepository.save(notificacion);
        }
    }

    public void enviarNotificacionPorCalificacionYInscripcion(Long usuarioId, String mensaje, Long calificacionId, Long inscripcionId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Calificacion> calificacionOpt = calificacionesRepository.findById(calificacionId);
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId); // Verificar inscripción

        if (usuarioOpt.isPresent() && calificacionOpt.isPresent()) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje(mensaje);
            notificacion.setEstado("ENVIADA");
            notificacion.setFechaNotificacion(java.time.LocalDateTime.now().toString());
            notificacion.setUsuario(usuarioOpt.get());
            notificacion.setLeida(false);
            notificacion.setCalificacion(calificacionOpt.get());

            if (inscripcionOpt.isPresent()) {
                notificacion.setInscripcion(inscripcionOpt.get());
            } else {
                notificacion.setInscripcion(null);
            }

            notificacionRepository.save(notificacion);
        }
    }



    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarNotificacion(Long notificacionId) {
        if (!notificacionRepository.existsById(notificacionId)) {
            throw new NoSuchElementException("Notificación no encontrada");
        }
        notificacionRepository.deleteById(notificacionId);
    }


    public void marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId).orElseThrow();
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerTodasNotificaciones() {
        return notificacionRepository.findAll();
    }
}
