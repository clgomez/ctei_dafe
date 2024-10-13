package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolProblemaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EfectoServiceImpl implements EfectoService {

    @Autowired
    private EfectoRepository efectoRepository;

    @Autowired
    private ArbolProblemaRepository arbolProblemaRepository;

    public EfectoDTO crearEfecto(EfectoDTO efectoDTO) {
        ArbolDeProblemas arbolDeProblemas = arbolProblemaRepository.findById(efectoDTO.getArbolDeProblemasId())
                .orElseThrow(() -> new IllegalArgumentException("El árbol de objetivos no existe con ID: " + efectoDTO.getArbolDeProblemasId()));

        Efecto efecto = new Efecto();
        efecto.setCalificacion(efectoDTO.getCalificacion());
        efecto.setNombre(efectoDTO.getNombre());
        efecto.setArbolProblemas(arbolDeProblemas);

        efectoRepository.save(efecto);
        return mapToDTO(efecto);
    }


    public List<EfectoDTO> obtenerTodos() {
        return efectoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public EfectoDTO obtenerPorId(Long id) {
        return efectoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Efecto no encontrado con ID: " + id));
    }

    public EfectoDTO actualizarMedio(Long id, EfectoDTO efectoDTO) {
        return efectoRepository.findById(id).map(causa -> {
            causa.setCalificacion(efectoDTO.getCalificacion());
            causa.setNombre(efectoDTO.getNombre());
            efectoRepository.save(causa);
            return mapToDTO(causa);
        }).orElseThrow(() -> new IllegalArgumentException("Efecto no encontrado con ID: " + id));
    }

    public boolean eliminarMedio(Long id) {
        if (efectoRepository.existsById(id)) {
            efectoRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Efecto no encontrado con ID: " + id);
    }

    public List<EfectoDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId) {
        List<Efecto> efectos = efectoRepository.findByArbolProblemas_Id(arbolDeObjetivosId);

        if (efectos.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron Efectos para el árbol de objetivos con ID: " + arbolDeObjetivosId);
        }

        return efectos.stream().map(this::mapToDTO).toList();
    }


    public EfectoDTO actualizarCalificacion(Long id, EfectoDTO efectoDTO) {
        return efectoRepository.findById(id).map(efecto -> {
            efecto.setCalificacion(efectoDTO.getCalificacion());
            efecto.setSemaforo(efectoDTO.getSemaforo());
            efecto.setComentario(efectoDTO.getComentario());

            efectoRepository.save(efecto);
            return mapToDTO(efecto);
        }).orElse(null);
    }



    private EfectoDTO mapToDTO(Efecto efecto) {
        EfectoDTO dto = new EfectoDTO();
        dto.setId(efecto.getId());
        dto.setCalificacion(efecto.getCalificacion());
        dto.setSemaforo(efecto.getSemaforo());
        dto.setNombre(efecto.getNombre());
        dto.setComentario(efecto.getComentario());

        if (efecto.getArbolProblemas() != null) {
            dto.setArbolDeProblemasId(efecto.getArbolProblemas().getId());
        }
        return dto;
    }

}
