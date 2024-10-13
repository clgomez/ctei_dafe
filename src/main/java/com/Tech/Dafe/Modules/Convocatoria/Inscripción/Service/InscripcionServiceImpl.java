package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Repository.ConvocatoriaRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository.InscripcionRepository;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private NotificacionService notificacionService;



    public Inscripcion inscribirProyecto(InscripcionProyectoDTO dto) {
        Optional<Convocatoria> convocatoriaOpt = convocatoriaRepository.findById(dto.getConvocatoriaId());
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(dto.getProyectoId());

        if (!convocatoriaOpt.isPresent()) {
            throw new IllegalArgumentException(new Mensaje("Convocatoria no encontrada.").getMensaje());
        }
        if (!proyectoOpt.isPresent()) {
            throw new IllegalArgumentException(new Mensaje("Proyecto no encontrado.").getMensaje());
        }

        Convocatoria convocatoria = convocatoriaOpt.get();
        LocalDate fechaInicio = LocalDate.parse(convocatoria.getFecha_inicio());
        LocalDate fechaFin = LocalDate.parse(convocatoria.getFecha_fin());
        LocalDate hoy = LocalDate.now();

        if (hoy.isBefore(fechaInicio) || hoy.isAfter(fechaFin)) {
            throw new IllegalArgumentException(new Mensaje("No se puede inscribir. La convocatoria no está abierta.").getMensaje());
        }

        // Verificar si el proyecto ya está inscrito en la convocatoria
        Optional<Inscripcion> existingInscripcion = inscripcionRepository.findByConvocatoriaAndProyecto(convocatoria, proyectoOpt.get());
        if (existingInscripcion.isPresent()) {
            throw new IllegalArgumentException(new Mensaje("El proyecto ya se encuentra inscrito a la convocatoria " + convocatoria.getDescripcion() + ".").getMensaje());
        }

        // Verificar si hay una inscripción en proceso
        Optional<Inscripcion> ongoingInscripcion = inscripcionRepository.findByProyectoAndEstado(proyectoOpt.get(), "PENDIENTE");
        if (ongoingInscripcion.isPresent()) {
            Convocatoria ongoingConvocatoria = ongoingInscripcion.get().getConvocatoria();
            throw new IllegalArgumentException(new Mensaje("El proyecto ya se encuentra en proceso de inscripción a la convocatoria " + ongoingConvocatoria.getDescripcion() + ".").getMensaje());
        }

        // Crear nueva inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setFechaInscripcion(hoy.toString());
        inscripcion.setConvocatoria(convocatoria);
        inscripcion.setProyecto(proyectoOpt.get());
        inscripcion.setUsuario(proyectoOpt.get().getUsuario());

        return inscripcionRepository.save(inscripcion);
    }




    public Inscripcion actualizarInscripcion(Long inscripcionId, InscripcionProyectoDTO dto) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new IllegalArgumentException(new Mensaje("Inscripción no encontrada con la ID: " + inscripcionId).getMensaje());
        }

        Inscripcion inscripcion = inscripcionOpt.get();
        String mensajeNotificacion = null;

        if (dto.getEstado() != null) {
            String estadoAnterior = inscripcion.getEstado();

            if (!estadoAnterior.equals(dto.getEstado())) {
                inscripcion.setEstado(dto.getEstado());
                mensajeNotificacion = "Su inscripción ha sido actualizada a: " + dto.getEstado();
            }
        }

        String fechaActualizacion = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        inscripcion.setFecha_actualizacion(fechaActualizacion);

        inscripcion.setConvocatoria(convocatoriaRepository.findById(dto.getConvocatoriaId())
                .orElseThrow(() -> new IllegalArgumentException(new Mensaje("Convocatoria no encontrada.").getMensaje())));

        Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                .orElseThrow(() -> new IllegalArgumentException(new Mensaje("Proyecto no encontrado.").getMensaje()));

        inscripcion.setProyecto(proyecto);

        Inscripcion actualizada = inscripcionRepository.save(inscripcion);

        if (mensajeNotificacion != null) {
            Usuario creadorProyecto = proyecto.getUsuario();
            notificacionService.enviarNotificacionesAUsuario(creadorProyecto.getId(), mensajeNotificacion);
        }

        return actualizada;
    }




    public void eliminarInscripcion(Long inscripcionId) {
        if (!inscripcionRepository.existsById(inscripcionId)) {
            throw new IllegalArgumentException("Inscripción no encontrada con la id: " + inscripcionId);
        }
        inscripcionRepository.deleteById(inscripcionId);
    }

    public List<Inscripcion> obtenerTodasInscripciones() {
        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        if (inscripciones.isEmpty()) {
            throw new NoSuchElementException("No hay inscripciones disponibles.");
        }
        return inscripciones;
    }


}
