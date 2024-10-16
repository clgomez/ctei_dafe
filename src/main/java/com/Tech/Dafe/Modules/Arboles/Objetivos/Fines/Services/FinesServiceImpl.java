package com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.DTO.FinesDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Repository.FinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinesServiceImpl implements FinesService {

    @Autowired
    private FinesRepository finesRepository;

    @Autowired
    private ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    public FinesDTO crearFines(FinesDTO finesDTO) {
        ArbolDeObjetivos arbolDeObjetivos = arbolDeObjetivosRepository.findById(finesDTO.getArbolDeObjetivosId())
                .orElseThrow(() -> new IllegalArgumentException("El árbol de objetivos no existe con ID: " + finesDTO.getArbolDeObjetivosId()));

        Fines fines = new Fines();
        fines.setCalificacion(finesDTO.getCalificacion());
        fines.setNombre(finesDTO.getNombre());
        fines.setArbolDeObjetivos(arbolDeObjetivos);

        finesRepository.save(fines);
        return mapToDTO(fines);
    }


    public List<FinesDTO> obtenerTodos() {
        return finesRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public FinesDTO obtenerPorId(Long id) {
        return finesRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Fine no encontrado con ID: " + id));
    }

    public FinesDTO actualizarMedio(Long id, FinesDTO finesDTO) {
        return finesRepository.findById(id).map(medio -> {
            medio.setCalificacion(finesDTO.getCalificacion());
            medio.setNombre(finesDTO.getNombre());
            finesRepository.save(medio);
            return mapToDTO(medio);
        }).orElseThrow(() -> new IllegalArgumentException("Fine no encontrado con ID: " + id));
    }

    public boolean eliminarMedio(Long id) {
        if (finesRepository.existsById(id)) {
            finesRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Fine no encontrado con ID: " + id);
    }

    public List<FinesDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId) {
        List<Fines> fines = finesRepository.findByArbolDeObjetivos_Id(arbolDeObjetivosId);

        if (fines.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron Fines para el árbol de objetivos con ID: " + arbolDeObjetivosId);
        }

        return fines.stream().map(this::mapToDTO).toList();
    }


    public FinesDTO actualizarCalificacion(Long id, FinesDTO finesDTO) {
        return finesRepository.findById(id).map(fines -> {
            fines.setCalificacion(finesDTO.getCalificacion());
            fines.setSemaforo(finesDTO.getSemaforo());
            fines.setComentario(finesDTO.getComentario());

            finesRepository.save(fines);
            return mapToDTO(fines);
        }).orElse(null);
    }


    private FinesDTO mapToDTO(Fines fines) {
        FinesDTO dto = new FinesDTO();
        dto.setId(fines.getId());
        dto.setCalificacion(fines.getCalificacion());
        dto.setNombre(fines.getNombre());
        dto.setSemaforo(fines.getSemaforo());
        dto.setComentario(fines.getComentario());

        if (fines.getArbolDeObjetivos() != null) {
            dto.setArbolDeObjetivosId(fines.getArbolDeObjetivos().getId());
        }
        return dto;
    }

}
