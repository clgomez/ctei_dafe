package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Services;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.DTO.ArbolDeProblemasDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolDeProblemasRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArbolDeProblemasServiceImpl implements ArbolDeProblemasService {

    private final ArbolDeProblemasRepository arbolDeProblemasRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    public ArbolDeProblemasDTO crearArbolDeProblemas(
            ArbolDeProblemasDTO arbolDTO,
            Long proyectoId) {

        if (arbolDeProblemasRepository.existsByProyectoId(proyectoId)) {
            throw new IllegalArgumentException(
                    "Ya existe un árbol de problemas asociado al proyecto con ID: "
                            + proyectoId);
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el proyecto con ID: " + proyectoId));

        ArbolDeProblemas arbol = new ArbolDeProblemas();
        arbol.setDescripcion(arbolDTO.getDescripcion());
        arbol.setProyecto(proyecto);

        arbol = arbolDeProblemasRepository.save(arbol);

        return mapToDTO(arbol);
    }

    @Override
    public ArbolDeProblemasDTO obtenerPorId(Long id) {

        ArbolDeProblemas arbol = arbolDeProblemasRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el árbol de problemas con ID: " + id));

        return mapToDTO(arbol);
    }

    @Override
    @Transactional
    public boolean eliminarArbolDeProblemas(Long id) {

        ArbolDeProblemas arbol = arbolDeProblemasRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el árbol de problemas con ID: " + id));

        Proyecto proyecto = arbol.getProyecto();

        if (proyecto != null) {
            proyecto.setArbolDeProblemas(null);
            arbol.setProyecto(null);
        }

        arbolDeProblemasRepository.delete(arbol);

        return true;
    }

    @Override
    public List<ArbolDeProblemasDTO> obtenerTodos() {

        return arbolDeProblemasRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ArbolDeProblemasDTO actualizarArbolDeProblemas(
            Long id,
            ArbolDeProblemasDTO arbolDTO) {

        ArbolDeProblemas arbol = arbolDeProblemasRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el árbol de problemas con ID: " + id));

        arbol.setDescripcion(arbolDTO.getDescripcion());

        arbol = arbolDeProblemasRepository.save(arbol);

        return mapToDTO(arbol);
    }

    @Override
    public ArbolDeProblemasDTO obtenerArbolDeProblemasPorIdProyecto(Long proyectoId) {

        return arbolDeProblemasRepository
                .findByProyectoId(proyectoId)
                .map(this::mapToDTO)
                .orElse(null);

    }
   
      

  /*@Override
    public ArbolDeProblemasDTO obtenerArbolDeProblemasPorIdProyecto(Long proyectoId) {

        ArbolDeProblemas arbol = arbolDeProblemasRepository
                .findByProyectoId(proyectoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró un árbol de problemas para el proyecto con ID: "
                                        + proyectoId));

        return mapToDTO(arbol);
    }
    */    

    private ArbolDeProblemasDTO mapToDTO(ArbolDeProblemas arbol) {

        ArbolDeProblemasDTO dto = new ArbolDeProblemasDTO();

        dto.setId(arbol.getId());
        dto.setDescripcion(arbol.getDescripcion());
        dto.setProyectoId(arbol.getProyecto().getId());
        dto.setSemaforo(arbol.getSemaforo());

        return dto;
    }

}