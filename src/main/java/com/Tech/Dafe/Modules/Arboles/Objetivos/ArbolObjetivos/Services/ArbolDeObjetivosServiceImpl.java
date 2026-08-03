package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ArbolDeObjetivosServiceImpl implements ArbolDeObjetivosService {

    private final ArbolDeObjetivosRepository arbolDeObjetivosRepository;
    
    private final ProyectoRepository proyectoRepository;

   @Override
    public ArbolDeObjetivosDTO crearArbolDeObjetivos(ArbolDeObjetivosDTO arbolDTO) {

        if (arbolDeObjetivosRepository.existsByProyectoId(arbolDTO.getProyectoId())) {
            throw new IllegalArgumentException(
                    "Ya existe un árbol de objetivos asociado al proyecto con ID: "
                            + arbolDTO.getProyectoId());
        }

        Proyecto proyecto = proyectoRepository.findById(arbolDTO.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el proyecto con ID: " + arbolDTO.getProyectoId()));

        ArbolDeObjetivos arbol = new ArbolDeObjetivos();
        arbol.setDescripcion(arbolDTO.getDescripcion());
        arbol.setProyecto(proyecto);

        arbolDeObjetivosRepository.save(arbol);

        return mapToDTO(arbol);
    }

    public List<ArbolDeObjetivosDTO> obtenerTodos() {
        return arbolDeObjetivosRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public ArbolDeObjetivosDTO obtenerPorId(Long id) {

        ArbolDeObjetivos arbol = arbolDeObjetivosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el árbol de objetivos con ID: " + id));

        return mapToDTO(arbol);
    }

    @Override
    public ArbolDeObjetivosDTO actualizarArbolDeObjetivos(
            Long id,
            ArbolDeObjetivosDTO arbolDTO) {

        ArbolDeObjetivos arbol = arbolDeObjetivosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el árbol de objetivos con ID: " + id));

        arbol.setDescripcion(arbolDTO.getDescripcion());

        arbolDeObjetivosRepository.save(arbol);

        return mapToDTO(arbol);
    }


    @Override
    @Transactional
    public boolean eliminarArbolDeObjetivos(Long id) {

        ArbolDeObjetivos arbol = arbolDeObjetivosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el árbol de objetivos con ID: " + id));

        Proyecto proyecto = arbol.getProyecto();

        if (proyecto != null) {
            proyecto.setArbolDeObjetivos(null);
            arbol.setProyecto(null);
        }

        arbolDeObjetivosRepository.delete(arbol);

        return true;
    }

    private ArbolDeObjetivosDTO mapToDTO(ArbolDeObjetivos arbol) {
        ArbolDeObjetivosDTO dto = new ArbolDeObjetivosDTO();
        dto.setId(arbol.getId());
        dto.setDescripcion(arbol.getDescripcion());
        dto.setProyectoId(arbol.getProyecto().getId());
        dto.setSemaforo(arbol.getSemaforo());
        return dto;
    }


    @Override
    public ArbolDeObjetivosDTO obtenerArbolDeObjetivosPorIdProyecto(Long proyectoId) {

        return arbolDeObjetivosRepository
                .findByProyectoId(proyectoId)
                .map(this::mapToDTO)
                .orElse(null);

    }

    /*
    @Override
    public ArbolDeObjetivos obtenerArbolDeObjetivosPorIdProyecto(Long proyectoId) {

        return arbolDeObjetivosRepository.findByProyectoId(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el árbol de objetivos para el proyecto con ID: " + proyectoId));
    }
    */


}
