package com.Tech.Dafe.Modules.Actividades.Actividades.Service;

import com.Tech.Dafe.Modules.Actividades.Actividades.DTO.ActividadDTO;
import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import com.Tech.Dafe.Modules.Actividades.Actividades.Repository.ActividadRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Repository.FinesRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadServiceImpl implements ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EfectoRepository efectoRepository;

    @Autowired
    private FinesRepository finesRepository;

    @Autowired
    private CausaRepository causaRepository;

    @Autowired
    private MedioRepository medioRepository;

    public ActividadDTO crearActividad(ActividadDTO actividadDTO) {
        Actividad actividad = new Actividad();
        actividad.setProyecto(proyectoRepository.findById(actividadDTO.getProyectoId()).orElse(null));
        actividad.setNombre(actividadDTO.getNombre());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setEfecto(efectoRepository.findById(actividadDTO.getEfectoId()).orElse(null));
        actividad.setFin(finesRepository.findById(actividadDTO.getFinId()).orElse(null));
        actividad.setCausa(causaRepository.findById(actividadDTO.getCausaId()).orElse(null));
        actividad.setMedio(medioRepository.findById(actividadDTO.getMedioId()).orElse(null));

        actividad = actividadRepository.save(actividad);
        actividadDTO.setId(actividad.getId());
        return actividadDTO;
    }

    public List<ActividadDTO> obtenerTodas() {
        return actividadRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<ActividadDTO> obtenerPorId(Long id) {
        return actividadRepository.findById(id).map(this::mapToDTO);
    }

    public ActividadDTO actualizarActividad(Long id, ActividadDTO actividadDTO) {
        return actividadRepository.findById(id).map(actividad -> {
            actividad.setProyecto(proyectoRepository.findById(actividadDTO.getProyectoId()).orElse(null));
            actividad.setNombre(actividadDTO.getNombre());
            actividad.setDescripcion(actividadDTO.getDescripcion());
            actividad.setEfecto(efectoRepository.findById(actividadDTO.getEfectoId()).orElse(null));
            actividad.setFin(finesRepository.findById(actividadDTO.getFinId()).orElse(null));
            actividad.setCausa(causaRepository.findById(actividadDTO.getCausaId()).orElse(null));
            actividad.setMedio(medioRepository.findById(actividadDTO.getMedioId()).orElse(null));

            actividadRepository.save(actividad);
            return mapToDTO(actividad);
        }).orElse(null);
    }

    public boolean eliminarActividad(Long id) {
        if (actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
            return true;
        }
        return false;
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
}
