package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models.AsignacionDeRoles;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Repository.AsignacionDeRolesRepository;
import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignacionDeRolesServiceImpl implements AsignacionDeRolesService {

    private final AsignacionDeRolesRepository asignacionDeRolesRepository;

    private final ProyectoRepository proyectoRepository;
    
    private final UsuarioRepository usuarioRepository;
     
    private final NotificacionService notificacionService;

    @Override
    public AsignacionDeRolesDTO crearAsignacion(
            AsignacionDeRolesDTO asignacionDTO) {

        if (asignacionDTO.getUsuarioTutorId() == null
                || asignacionDTO.getUsuarioEvaluadorId() == null
                || asignacionDTO.getProyectoId() == null) {

            throw new IllegalArgumentException(
                    "Los IDs del tutor, evaluador y proyecto son obligatorios");
        }

        Proyecto proyecto = proyectoRepository.findById(
                asignacionDTO.getProyectoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Proyecto no encontrado con ID: "
                                        + asignacionDTO.getProyectoId()));

        Usuario investigador = proyecto.getUsuario();

        if (investigador == null) {
            throw new BusinessException(
                    "El proyecto no tiene un investigador asignado");
        }

        Usuario tutor = usuarioRepository.findById(
                asignacionDTO.getUsuarioTutorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tutor no encontrado con ID: "
                                        + asignacionDTO.getUsuarioTutorId()));

        Usuario evaluador = usuarioRepository.findById(
                asignacionDTO.getUsuarioEvaluadorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Evaluador no encontrado con ID: "
                                        + asignacionDTO.getUsuarioEvaluadorId()));

        AsignacionDeRoles asignacion = new AsignacionDeRoles();

        asignacion.setEstado(asignacionDTO.getEstado());
        asignacion.setFechaAsignacion(LocalDateTime.now());

        asignacion.setProyecto(proyecto);
        asignacion.setUsuarioInvestigador(investigador);
        asignacion.setUsuarioTutor(tutor);
        asignacion.setUsuarioEvaluador(evaluador);

        asignacion = asignacionDeRolesRepository.save(asignacion);

        notificacionService.enviarNotificacionesAUsuario(
                investigador.getId(),
                "Se le ha asignado un tutor y un evaluador para hacer seguimiento a su proyecto");

        notificacionService.enviarNotificacionesAUsuario(
                tutor.getId(),
                "Se le ha asignado un proyecto para tutoría");

        notificacionService.enviarNotificacionesAUsuario(
                evaluador.getId(),
                "Se le ha asignado un proyecto para evaluación");

        return mapToDTO(asignacion);
    }


    @Override
    public Optional<AsignacionDeRolesDTO> obtenerAsignacion(Long id) {
        return asignacionDeRolesRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public AsignacionDeRolesDTO actualizarAsignacion(
            Long id,
            AsignacionDeRolesDTO dto) {

        AsignacionDeRoles asignacion =
                asignacionDeRolesRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asignación no encontrada con ID: " + id));

        Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Proyecto no encontrado con ID: "
                                        + dto.getProyectoId()));

        Usuario tutor = usuarioRepository.findById(dto.getUsuarioTutorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tutor no encontrado con ID: "
                                        + dto.getUsuarioTutorId()));

        Usuario evaluador = usuarioRepository.findById(dto.getUsuarioEvaluadorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Evaluador no encontrado con ID: "
                                        + dto.getUsuarioEvaluadorId()));

        asignacion.setEstado(dto.getEstado());
        asignacion.setFechaActualizacion(LocalDateTime.now());

        asignacion.setProyecto(proyecto);
        asignacion.setUsuarioInvestigador(proyecto.getUsuario());
        asignacion.setUsuarioTutor(tutor);
        asignacion.setUsuarioEvaluador(evaluador);

        asignacion = asignacionDeRolesRepository.save(asignacion);

        notificacionService.enviarNotificacionesAUsuario(
                proyecto.getUsuario().getId(),
                "Se le ha reasignado un tutor y un evaluador para su proyecto");

        notificacionService.enviarNotificacionesAUsuario(
                tutor.getId(),
                "Se le ha reasignado un proyecto para tutoría");

        notificacionService.enviarNotificacionesAUsuario(
                evaluador.getId(),
                "Se le ha reasignado un proyecto para evaluación");

        return mapToDTO(asignacion);
    }


    @Override
    public void eliminarAsignacion(Long id) {

        AsignacionDeRoles asignacion =
                asignacionDeRolesRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asignación no encontrada con ID: " + id));

        asignacionDeRolesRepository.delete(asignacion);
    }

    @Override
    public List<AsignacionDeRolesDTO> listarAsignaciones() {
        return asignacionDeRolesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

 private AsignacionDeRolesDTO mapToDTO(AsignacionDeRoles asignacion) {

    AsignacionDeRolesDTO dto = new AsignacionDeRolesDTO();

    dto.setId(asignacion.getId());
    dto.setEstado(asignacion.getEstado());
    dto.setFechaAsignacion(asignacion.getFechaAsignacion());
    dto.setFechaActualizacion(asignacion.getFechaActualizacion());

    if (asignacion.getProyecto() != null) {
        dto.setProyectoId(asignacion.getProyecto().getId());
    }

    if (asignacion.getUsuarioInvestigador() != null) {
        dto.setUsuarioInvestigadorId(
                asignacion.getUsuarioInvestigador().getId());
    }

    if (asignacion.getUsuarioTutor() != null) {
        dto.setUsuarioTutorId(
                asignacion.getUsuarioTutor().getId());
    }

    if (asignacion.getUsuarioEvaluador() != null) {
        dto.setUsuarioEvaluadorId(
                asignacion.getUsuarioEvaluador().getId());
    }

    return dto;
}

   @Override
    public List<AsignacionDeRolesDTO>
    obtenerAsignacionesDeRolesPorIdUsuarioInvestigador(Long id) {

        usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario investigador no encontrado con ID: " + id));

        return asignacionDeRolesRepository
                .findByUsuarioInvestigadorId(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

   @Override
    public List<AsignacionDeRolesDTO>
    obtenerAsignacionesDeRolesPorIdUsuarioTutor(Long id) {

        usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario tutor no encontrado con ID: " + id));

        return asignacionDeRolesRepository
                .findByUsuarioTutorId(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AsignacionDeRolesDTO> obtenerAsignacionDeRolesPorIdProyecto(Long ProyectoId) {
        return asignacionDeRolesRepository.findByProyectoId(ProyectoId)
        .map(this::mapToDTO);
    }


    @Override
    public Optional<AsignacionDeRolesDTO> obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioTutor(
            Long proyectoId, Long usuarioInvestigadorId, Long usuarioTutorId) {
         
        return asignacionDeRolesRepository.findByProyectoIdAndUsuarioInvestigadorIdAndUsuarioTutorId(proyectoId, usuarioInvestigadorId, usuarioTutorId).map(this::mapToDTO);
    }

    @Override
    public Optional<AsignacionDeRolesDTO> obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioEvaluador(
            Long proyectoId, Long usuarioInvestigadorId, Long usuarioEvaluadorId) {
         
        return asignacionDeRolesRepository.findByProyectoIdAndUsuarioInvestigadorIdAndUsuarioEvaluadorId(proyectoId, usuarioInvestigadorId, usuarioEvaluadorId).map(this::mapToDTO);
    }

 
}
