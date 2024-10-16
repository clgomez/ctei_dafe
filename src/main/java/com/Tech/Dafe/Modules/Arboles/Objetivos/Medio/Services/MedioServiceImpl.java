package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioServiceImpl implements MedioService {

    @Autowired
    private MedioRepository medioRepository;

    @Autowired
    private ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    public MedioDTO crearMedio(MedioDTO medioDTO) {
        ArbolDeObjetivos arbolDeObjetivos = arbolDeObjetivosRepository.findById(medioDTO.getArbolDeObjetivosId())
                .orElseThrow(() -> new IllegalArgumentException("El árbol de objetivos no existe con ID: " + medioDTO.getArbolDeObjetivosId()));

        Medio medio = new Medio();
        medio.setCalificacion(medioDTO.getCalificacion());
        medio.setNombre(medioDTO.getNombre());
        medio.setArbolDeObjetivos(arbolDeObjetivos);

        medioRepository.save(medio);
        return mapToDTO(medio);
    }


    public List<MedioDTO> obtenerTodos() {
        return medioRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public MedioDTO obtenerPorId(Long id) {
        return medioRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Medio no encontrado con ID: " + id));
    }

    public MedioDTO actualizarMedio(Long id, MedioDTO medioDTO) {
        return medioRepository.findById(id).map(medio -> {
            medio.setCalificacion(medioDTO.getCalificacion());
            medio.setNombre(medioDTO.getNombre());
            medioRepository.save(medio);
            return mapToDTO(medio);
        }).orElseThrow(() -> new IllegalArgumentException("Medio no encontrado con ID: " + id));
    }

    public boolean eliminarMedio(Long id) {
        if (medioRepository.existsById(id)) {
            medioRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Medio no encontrado con ID: " + id);
    }

    public List<MedioDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId) {
        List<Medio> medios = medioRepository.findByArbolDeObjetivos_Id(arbolDeObjetivosId);

        if (medios.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron medios para el árbol de objetivos con ID: " + arbolDeObjetivosId);
        }

        return medios.stream().map(this::mapToDTO).toList();
    }

    public MedioDTO actualizarCalificacion(Long id, MedioDTO medioDTO) {
        return medioRepository.findById(id).map(medio -> {
            medio.setCalificacion(medioDTO.getCalificacion());
            medio.setSemaforo(medioDTO.getSemaforo());
            medio.setComentario(medioDTO.getComentario());

            medioRepository.save(medio);
            return mapToDTO(medio);
        }).orElse(null);
    }

    private MedioDTO mapToDTO(Medio medio) {
        MedioDTO dto = new MedioDTO();
        dto.setId(medio.getId());
        dto.setCalificacion(medio.getCalificacion());
        dto.setSemaforo(medio.getSemaforo());
        dto.setNombre(medio.getNombre());
        dto.setComentario(medio.getComentario());

        if (medio.getArbolDeObjetivos() != null) {
            dto.setArbolDeObjetivosId(medio.getArbolDeObjetivos().getId());
        }
        return dto;
    }
}
