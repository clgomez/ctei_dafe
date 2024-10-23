package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models.AsignacionDeRoles;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Repository.Asignacion_rolesRepository;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignacionrolesServiceImpl implements AsignacionDeRolesService {

    @Autowired
    private Asignacion_rolesRepository asignacionDeRolesRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AsignacionDeRolesDTO crearAsignacion(AsignacionDeRolesDTO asignacionDTO) {
        if (asignacionDTO.getUsuarioTutorId() == null ||
                asignacionDTO.getUsuarioEvaluadorId() == null ||
                asignacionDTO.getProyectoId() == null) {
            throw new IllegalArgumentException("Los IDs de usuario tutor, evaluador y proyecto no pueden ser nulos");
        }

        AsignacionDeRoles asignacion = new AsignacionDeRoles();
        asignacion.setEstado(asignacionDTO.getEstado());
        asignacion.setFecha_asignacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Proyecto proyecto = proyectoRepository.findById(asignacionDTO.getProyectoId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + asignacionDTO.getProyectoId()));
        asignacion.setProyecto(proyecto);

        Usuario usuarioInvestigador = proyecto.getUsuario();
        if (usuarioInvestigador == null) {
            throw new IllegalStateException("El proyecto no tiene un usuario asignado");
        }
        asignacion.setUsuarioInvestigador(usuarioInvestigador);

        asignacion.setUsuarioTutor(usuarioRepository.findById(asignacionDTO.getUsuarioTutorId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario tutor no encontrado con ID: " + asignacionDTO.getUsuarioTutorId())));
        asignacion.setUsuarioEvaluador(usuarioRepository.findById(asignacionDTO.getUsuarioEvaluadorId())
                .orElseThrow(() -> new IllegalArgumentException("Evaluador no encontrado con ID: " + asignacionDTO.getUsuarioEvaluadorId())));

        asignacion = asignacionDeRolesRepository.save(asignacion);
        return mapToDTO(asignacion);
    }


    @Override
    public Optional<AsignacionDeRolesDTO> obtenerAsignacion(Long id) {
        return asignacionDeRolesRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public AsignacionDeRolesDTO actualizarAsignacion(Long id, AsignacionDeRolesDTO asignacionActualizada) {
        return asignacionDeRolesRepository.findById(id).map(asignacion -> {
            asignacion.setEstado(asignacionActualizada.getEstado());

            String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            asignacion.setFecha_actualizacion(fechaActual);

            Proyecto proyecto = proyectoRepository.findById(asignacionActualizada.getProyectoId())
                    .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + asignacionActualizada.getProyectoId()));
            asignacion.setProyecto(proyecto);

            asignacion.setUsuarioTutor(usuarioRepository.findById(asignacionActualizada.getUsuarioTutorId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario tutor no encontrado con ID: " + asignacionActualizada.getUsuarioTutorId())));
            asignacion.setUsuarioEvaluador(usuarioRepository.findById(asignacionActualizada.getUsuarioEvaluadorId())
                    .orElseThrow(() -> new IllegalArgumentException("Evaluador no encontrado con ID: " + asignacionActualizada.getUsuarioEvaluadorId())));

            return mapToDTO(asignacionDeRolesRepository.save(asignacion));
        }).orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada con ID: " + id));
    }


    @Override
    public void eliminarAsignacion(Long id) {
        asignacionDeRolesRepository.deleteById(id);
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
        dto.setFechaAsignacion(asignacion.getFecha_asignacion());
        dto.setFechaActualizacion(asignacion.getFecha_actualizacion());
        dto.setProyectoId(asignacion.getProyecto().getId());
        dto.setUsuarioTutorId(asignacion.getUsuarioTutor().getId());
        dto.setUsuarioEvaluadorId(asignacion.getUsuarioEvaluador().getId());
        return dto;
    }
}
