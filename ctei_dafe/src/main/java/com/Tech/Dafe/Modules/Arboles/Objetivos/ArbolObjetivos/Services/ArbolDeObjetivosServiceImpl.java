package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.DTO.ArbolDeObjetivosDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbolDeObjetivosServiceImpl implements ArbolDeObjetivosService {

    @Autowired
    private ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    public ArbolDeObjetivosDTO crearArbolDeObjetivos(ArbolDeObjetivosDTO arbolDTO) {

        if (arbolDeObjetivosRepository.existsByProyectoId(arbolDTO.getProyectoId())) {
            throw new IllegalArgumentException("Ya existe un árbol de objetivos asociado al proyecto con ID: " + arbolDTO.getProyectoId());
        }

        var proyecto = proyectoRepository.findById(arbolDTO.getProyectoId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + arbolDTO.getProyectoId()));

        ArbolDeObjetivos arbol = new ArbolDeObjetivos();
        arbol.setNombre(arbolDTO.getNombre());
        arbol.setDescripcion(arbolDTO.getDescripcion());
        arbol.setProyecto(proyecto);

        arbolDeObjetivosRepository.save(arbol);

        arbolDTO.setId(arbol.getId());
        return mapToDTO(arbol);
    }



    public List<ArbolDeObjetivosDTO> obtenerTodos() {
        return arbolDeObjetivosRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<ArbolDeObjetivosDTO> obtenerPorId(Long id) {
        return arbolDeObjetivosRepository.findById(id).map(this::mapToDTO);
    }

    public ArbolDeObjetivosDTO actualizarArbolDeObjetivos(Long id, ArbolDeObjetivosDTO arbolDTO) {
        return arbolDeObjetivosRepository.findById(id).map(arbol -> {
            arbol.setNombre(arbolDTO.getNombre());
            arbol.setDescripcion(arbolDTO.getDescripcion());
            arbolDeObjetivosRepository.save(arbol);
            return mapToDTO(arbol);
        }).orElse(null);
    }

    public ArbolDeObjetivosDTO actualizarCalificacion(Long id, ArbolDeObjetivosDTO arbolDTO) {
        return arbolDeObjetivosRepository.findById(id).map(arbol -> {
            arbol.setCalificacion(arbolDTO.getCalificacion());
            arbol.setSemaforo(arbolDTO.getSemaforo());
            arbol.setComentario(arbolDTO.getComentario());

            arbolDeObjetivosRepository.save(arbol);
            return mapToDTO(arbol);
        }).orElse(null);
    }



    public boolean eliminarArbolDeObjetivos(Long id) {
        if (arbolDeObjetivosRepository.existsById(id)) {
            arbolDeObjetivosRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ArbolDeObjetivosDTO mapToDTO(ArbolDeObjetivos arbol) {
        ArbolDeObjetivosDTO dto = new ArbolDeObjetivosDTO();
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
