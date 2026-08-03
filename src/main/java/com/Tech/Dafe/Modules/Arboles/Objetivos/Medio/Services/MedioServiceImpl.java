/*package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedioServiceImpl implements MedioService {

    private final MedioRepository medioRepository;

    private final ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    public MedioDTO crearMedio(MedioDTO medioDTO) {
        ArbolDeObjetivos arbolDeObjetivos = arbolDeObjetivosRepository.findById(medioDTO.getArbolDeObjetivosId())
                .orElseThrow(() -> new IllegalArgumentException("El árbol de objetivos no existe con ID: " + medioDTO.getArbolDeObjetivosId()));

        Medio medio = new Medio();
        medio.setDescripcion(medioDTO.getDescripcion());
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
            medio.setDescripcion(medioDTO.getDescripcion());
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

    public List<MedioDTO> obtenerMediosPorArbolDeObjetivosId(Long arbolDeObjetivosId) {
        List<Medio> medios = medioRepository.findByArbolDeObjetivosId(arbolDeObjetivosId);
   

        return medios.stream().map(this::mapToDTO).toList();
    }

    public MedioDTO actualizarCalificacion(Long id, MedioDTO medioDTO) {
        return medioRepository.findById(id).map(medio -> {
            medio.setSemaforo(medioDTO.getSemaforo());
            medioRepository.save(medio);
            return mapToDTO(medio);
        }).orElse(null);
    }

    private MedioDTO mapToDTO(Medio medio) {
        MedioDTO dto = new MedioDTO();
        dto.setId(medio.getId());
        dto.setSemaforo(medio.getSemaforo());
        dto.setDescripcion(medio.getDescripcion());

        if (medio.getArbolDeObjetivos() != null) {
            dto.setArbolDeObjetivosId(medio.getArbolDeObjetivos().getId());
        }
        return dto;
    }
}
*/


package com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Services;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.DTO.MedioDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedioServiceImpl implements MedioService {

    private final MedioRepository medioRepository;
    private final ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    @Override
    public MedioDTO crearMedio(MedioDTO medioDTO) {

        ArbolDeObjetivos arbolDeObjetivos = arbolDeObjetivosRepository.findById(medioDTO.getArbolDeObjetivosId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un árbol de objetivos con ID: " + medioDTO.getArbolDeObjetivosId()));

        Medio medio = new Medio();
        medio.setDescripcion(medioDTO.getDescripcion());
        medio.setArbolDeObjetivos(arbolDeObjetivos);

        medioRepository.save(medio);

        return mapToDTO(medio);
    }

    @Override
    public List<MedioDTO> obtenerTodos() {
        return medioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public MedioDTO obtenerPorId(Long id) {

        Medio medio = medioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el medio con ID: " + id));

        return mapToDTO(medio);
    }

    @Override
    public MedioDTO actualizarMedio(Long id, MedioDTO medioDTO) {

        Medio medio = medioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el medio con ID: " + id));

        medio.setDescripcion(medioDTO.getDescripcion());

        medioRepository.save(medio);

        return mapToDTO(medio);
    }

    @Override
    public boolean eliminarMedio(Long id) {

        Medio medio = medioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el medio con ID: " + id));

        medioRepository.delete(medio);

        return true;
    }

    @Override
    public List<MedioDTO> obtenerMediosPorArbolDeObjetivosId(Long arbolDeObjetivosId) {

        arbolDeObjetivosRepository.findById(arbolDeObjetivosId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un árbol de objetivos con ID: " + arbolDeObjetivosId));

        return medioRepository.findByArbolDeObjetivosId(arbolDeObjetivosId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private MedioDTO mapToDTO(Medio medio) {

        MedioDTO dto = new MedioDTO();

        dto.setId(medio.getId());
        dto.setDescripcion(medio.getDescripcion());
        dto.setSemaforo(medio.getSemaforo());

        if (medio.getArbolDeObjetivos() != null) {
            dto.setArbolDeObjetivosId(medio.getArbolDeObjetivos().getId());
        }

        return dto;
    }
}