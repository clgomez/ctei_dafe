package com.Tech.Dafe.Modules.Actividades.Actividades.Service;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;
import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import com.Tech.Dafe.Modules.Actividades.Actividades.Repository.ActividadRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Repository.FinRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;

    private final ProyectoRepository proyectoRepository;

    private final EfectoRepository efectoRepository;

    private final FinRepository finRepository;

    private final CausaRepository causaRepository;

    private final MedioRepository medioRepository;

@Override
public ActividadDTO crearActividad(ActividadDTO actividadDTO) {

    Actividad actividad = new Actividad();

    actividad.setProyecto(
            proyectoRepository.findById(actividadDTO.getProyectoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el proyecto con ID: " + actividadDTO.getProyectoId()))
    );

    actividad.setEfecto(
            efectoRepository.findById(actividadDTO.getEfectoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el efecto con ID: " + actividadDTO.getEfectoId()))
    );

    actividad.setFin(
            finRepository.findById(actividadDTO.getFinId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el fin con ID: " + actividadDTO.getFinId()))
    );

    actividad.setCausa(
            causaRepository.findById(actividadDTO.getCausaId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró la causa con ID: " + actividadDTO.getCausaId()))
    );

    actividad.setMedio(
            medioRepository.findById(actividadDTO.getMedioId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el medio con ID: " + actividadDTO.getMedioId()))
    );

    actividad.setNombre(actividadDTO.getNombre());
    actividad.setDescripcion(actividadDTO.getDescripcion());

    actividad = actividadRepository.save(actividad);

    return mapToDTO(actividad);
}

public List<ActividadDTO> obtenerTodas() {
    return actividadRepository.findAll().stream().map(this::mapToDTO).toList();
}

@Override
public ActividadDTO obtenerPorId(Long id) {

    Actividad actividad = actividadRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "No se encontró la actividad con ID: " + id));

    return mapToDTO(actividad);
}

@Override
public ActividadDTO actualizarActividad(Long id, ActividadDTO actividadDTO) {

    Actividad actividad = actividadRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "No se encontró la actividad con ID: " + id));

    actividad.setProyecto(
            proyectoRepository.findById(actividadDTO.getProyectoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el proyecto con ID: " + actividadDTO.getProyectoId()))
    );

    actividad.setEfecto(
            efectoRepository.findById(actividadDTO.getEfectoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el efecto con ID: " + actividadDTO.getEfectoId()))
    );

    actividad.setFin(
            finRepository.findById(actividadDTO.getFinId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el fin con ID: " + actividadDTO.getFinId()))
    );

    actividad.setCausa(
            causaRepository.findById(actividadDTO.getCausaId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró la causa con ID: " + actividadDTO.getCausaId()))
    );

    actividad.setMedio(
            medioRepository.findById(actividadDTO.getMedioId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No se encontró el medio con ID: " + actividadDTO.getMedioId()))
    );

    actividad.setNombre(actividadDTO.getNombre());
    actividad.setDescripcion(actividadDTO.getDescripcion());

    actividadRepository.save(actividad);

    return mapToDTO(actividad);
}

@Override
public void eliminarActividad(Long id) {

    if (!actividadRepository.existsById(id)) {
        throw new ResourceNotFoundException(
                "No se encontró la actividad con ID: " + id);
    }

    actividadRepository.deleteById(id);
}

private ActividadDTO mapToDTO(Actividad actividad) {
    ActividadDTO dto = new ActividadDTO();
    dto.setId(actividad.getId());
    dto.setNombre(actividad.getNombre());
    dto.setDescripcion(actividad.getDescripcion());
    dto.setProyectoId(actividad.getProyecto().getId());
    dto.setEfectoId(actividad.getEfecto().getId());
    dto.setFinId(actividad.getFin().getId());
    dto.setCausaId(actividad.getCausa().getId());
    dto.setMedioId(actividad.getMedio().getId());
    return dto;
}

@Override
public List<ActividadDTO> obtenerActividadesPorIdProyecto(Long proyectoId) {

    if (!proyectoRepository.existsById(proyectoId)) {
        throw new ResourceNotFoundException(
                "No se encontró el proyecto con ID: " + proyectoId);
    }

    return actividadRepository.findByProyectoId(proyectoId)
            .stream()
            .map(this::mapToDTO)
            .toList();
  }
}
