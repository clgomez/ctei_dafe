package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolProblemasDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolProblemaRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbolDeProblemasServicesImpl implements ArbolDeProblemasServices {

    @Autowired
    private ArbolProblemaRepository arbolDeProblemasRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    public ArbolProblemasDTO crearArbolDeProblemas(ArbolProblemasDTO arbolDTO, Long proyectoId) {

        if (arbolDeProblemasRepository.existsByProyectoId(arbolDTO.getProyectoId())) {
            throw new IllegalArgumentException("Ya existe un árbol de problemas asociado al proyecto con ID: " + arbolDTO.getProyectoId());
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado."));

        ArbolDeProblemas arbol = new ArbolDeProblemas();
        arbol.setNombre(arbolDTO.getNombre());
        arbol.setDescripcion(arbolDTO.getDescripcion());
        arbol.setProyecto(proyecto);

        arbolDeProblemasRepository.save(arbol);
        return mapToDTO(arbol);
    }


    public ArbolProblemasDTO obtenerPorId(Long id) {
        return arbolDeProblemasRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public boolean eliminarArbolDeProblemas(Long id) {
        if (arbolDeProblemasRepository.existsById(id)) {
            arbolDeProblemasRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ArbolProblemasDTO> obtenerTodos() {
        return arbolDeProblemasRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ArbolProblemasDTO actualizarArbolDeProblemas(Long id, ArbolProblemasDTO arbolDTO) {
        return arbolDeProblemasRepository.findById(id).map(arbol -> {
            arbol.setNombre(arbolDTO.getNombre());
            arbol.setDescripcion(arbolDTO.getDescripcion());
            arbolDeProblemasRepository.save(arbol);
            return mapToDTO(arbol);
        }).orElse(null);
    }


    public ArbolProblemasDTO actualizarCalificacion(Long id, ArbolProblemasDTO arbolDTO) {
        return arbolDeProblemasRepository.findById(id).map(arbol -> {
            arbol.setCalificacion(arbolDTO.getCalificacion());
            arbol.setSemaforo(arbolDTO.getSemaforo());
            arbol.setComentario(arbolDTO.getComentario());

            arbolDeProblemasRepository.save(arbol);
            return mapToDTO(arbol);
        }).orElse(null);
    }


    private ArbolProblemasDTO mapToDTO(ArbolDeProblemas arbol) {
        ArbolProblemasDTO dto = new ArbolProblemasDTO();
        dto.setId(arbol.getId());
        dto.setNombre(arbol.getNombre());
        dto.setDescripcion(arbol.getDescripcion());
        dto.setProyectoId(arbol.getProyecto().getId());
        dto.setCalificacion(arbol.getCalificacion());
        dto.setComentario(arbol.getComentario());
        dto.setSemaforo(arbol.getSemaforo());
        return dto;
    }
}
