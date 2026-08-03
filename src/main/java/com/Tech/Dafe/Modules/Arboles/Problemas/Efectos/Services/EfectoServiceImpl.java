package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Services;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolDeProblemasRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.DTO.EfectoDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EfectoServiceImpl implements EfectoService {

    private final EfectoRepository efectoRepository;
    private final ArbolDeProblemasRepository arbolProblemaRepository;

    @Override
    public EfectoDTO crearEfecto(EfectoDTO efectoDTO) {

        ArbolDeProblemas arbolDeProblemas = arbolProblemaRepository
                .findById(efectoDTO.getArbolDeProblemasId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el árbol de problemas con ID: "
                                        + efectoDTO.getArbolDeProblemasId()));

        Efecto efecto = new Efecto();
        efecto.setDescripcion(efectoDTO.getDescripcion());
        efecto.setArbolProblemas(arbolDeProblemas);

        efectoRepository.save(efecto);

        return mapToDTO(efecto);
    }

    @Override
    public List<EfectoDTO> obtenerTodos() {
        return efectoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public EfectoDTO obtenerPorId(Long id) {

        Efecto efecto = efectoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el efecto con ID: " + id));

        return mapToDTO(efecto);
    }

    @Override
    public EfectoDTO actualizarEfecto(Long id, EfectoDTO efectoDTO) {

        Efecto efecto = efectoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el efecto con ID: " + id));

        efecto.setDescripcion(efectoDTO.getDescripcion());

        efectoRepository.save(efecto);

        return mapToDTO(efecto);
    }

    @Override
    public boolean eliminarEfecto(Long id) {

        Efecto efecto = efectoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el efecto con ID: " + id));

        efectoRepository.delete(efecto);

        return true;
    }

    @Override
    public List<EfectoDTO> obtenerEfectosPorArbolDeProblemasId(Long arbolDeProblemasId) {

        if (!arbolProblemaRepository.existsById(arbolDeProblemasId)) {
            throw new ResourceNotFoundException(
                    "No existe el árbol de problemas con ID: " + arbolDeProblemasId);
        }

        return efectoRepository.findByArbolProblemasId(arbolDeProblemasId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private EfectoDTO mapToDTO(Efecto efecto) {

        EfectoDTO dto = new EfectoDTO();
        dto.setId(efecto.getId());
        dto.setDescripcion(efecto.getDescripcion());
        dto.setSemaforo(efecto.getSemaforo());

        if (efecto.getArbolProblemas() != null) {
            dto.setArbolDeProblemasId(efecto.getArbolProblemas().getId());
        }

        return dto;
    }
}