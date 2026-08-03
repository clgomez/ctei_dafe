package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Repository.ConvocatoriaRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionProyectoDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository.InscripcionRepository;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    private final ConvocatoriaRepository convocatoriaRepository;

    private final ProyectoRepository proyectoRepository;

    private final NotificacionService notificacionService;

    @Override
    public Inscripcion inscribirProyecto(InscripcionProyectoDTO dto) {

        Convocatoria convocatoria = convocatoriaRepository.findById(dto.getConvocatoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Convocatoria no encontrada."));

        Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Proyecto no encontrado."));

        LocalDate hoy = LocalDate.now();

        if (hoy.isBefore(convocatoria.getFechaInicio())
                || hoy.isAfter(convocatoria.getFechaFin())) {

            throw new BusinessException( "No se puede inscribir. La convocatoria no está abierta.");
        }

        if (inscripcionRepository
                .findByConvocatoriaAndProyecto(convocatoria, proyecto)
                .isPresent()) {

            throw new BusinessException("El proyecto ya se encuentra inscrito a la convocatoria: "
                                        + convocatoria.getDescripcion() + ".");
        }

        Optional<Inscripcion> pendiente =
                inscripcionRepository.findByProyectoAndEstado(proyecto, "PENDIENTE");

        if (pendiente.isPresent()) {

            throw new BusinessException("El proyecto ya se encuentra en proceso de inscripción a la convocatoria: "
                                        + pendiente.get().getConvocatoria().getDescripcion() + ".");
        }

        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setConvocatoria(convocatoria);
        inscripcion.setProyecto(proyecto);
        inscripcion.setUsuario(proyecto.getUsuario());

        Inscripcion nueva = inscripcionRepository.save(inscripcion);

        notificacionService.enviarNotificacionesPorRol(
                RolNombre.ROL_ADMINISTRADOR,
                "Nueva inscripción creada a convocatoria: "
                        + nueva.getConvocatoria().getDescripcion()
                        + " del proyecto: "
                        + nueva.getProyecto().getTitulo());

        return nueva;
    }



    @Override
    public Inscripcion actualizarInscripcion(Long inscripcionId, InscripcionProyectoDTO dto) {

        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inscripción no encontrada con ID: " + inscripcionId));

        Convocatoria convocatoria = convocatoriaRepository.findById(dto.getConvocatoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Convocatoria no encontrada."));

        Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Proyecto no encontrado."));

        String mensajeNotificacion = null;

        if (dto.getEstado() != null &&
                !dto.getEstado().equals(inscripcion.getEstado())) {

            inscripcion.setEstado(dto.getEstado());
            mensajeNotificacion =
                    "Su inscripción ha sido actualizada a: " + dto.getEstado();
        }

        inscripcion.setFechaActualizacion(LocalDateTime.now());
        inscripcion.setConvocatoria(convocatoria);
        inscripcion.setProyecto(proyecto);

        Inscripcion actualizada = inscripcionRepository.save(inscripcion);

        if (mensajeNotificacion != null) {
            notificacionService.enviarNotificacionesAUsuario(
                    proyecto.getUsuario().getId(),
                    mensajeNotificacion);
        }

        return actualizada;
    }

    @Override
    public void eliminarInscripcion(Long inscripcionId) {

        if (!inscripcionRepository.existsById(inscripcionId)) {
            throw new ResourceNotFoundException(
                    "Inscripción no encontrada con ID: " + inscripcionId);
        }

        inscripcionRepository.deleteById(inscripcionId);
    }


    public List<InscripcionDTO> obtenerTodasInscripciones() {
       return inscripcionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
     
    }


    @Override
    public Optional<Inscripcion> obtenerInscripcion(Long inscripcionId) {
    
        return inscripcionRepository.findById(inscripcionId);

    }


    @Override
    public List<InscripcionDTO> obtenerInscripcionesPorIdUsuario(Long usuarioId) {

        return inscripcionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }


     private InscripcionDTO mapToDTO(Inscripcion inscripcion) {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setFechaActualizacion(inscripcion.getFechaActualizacion());
        dto.setEstado(inscripcion.getEstado());
        dto.setUsuarioId(inscripcion.getUsuario().getId());
        dto.setProyectoId(inscripcion.getProyecto().getId());
        dto.setConvocatoriaId(inscripcion.getConvocatoria().getId());
        return dto;
    }

   @Override
    public InscripcionDTO obtenerInscripcionPorIdProyectoYIdUsuarioInvestigador(
            Long proyectoId,
            Long usuarioInvestigadorId) {

        return inscripcionRepository
                .findByProyectoIdAndUsuarioId(proyectoId, usuarioInvestigadorId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró una inscripción para el proyecto con ID: "
                                + proyectoId
                                + " y el usuario con ID: "
                                + usuarioInvestigadorId));
    }


}
