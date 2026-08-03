package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services;

import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolDeProblemasRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CausaServiceImpl implements CausaService {

    private final CausaRepository causaRepository;
    private final ArbolDeProblemasRepository arbolProblemaRepository;

    @Override
    public CausaDTO crearCausa(CausaDTO causaDTO) {

        ArbolDeProblemas arbolDeProblemas = arbolProblemaRepository.findById(causaDTO.getArbolDeProblemasId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el árbol de problemas con ID: " + causaDTO.getArbolDeProblemasId()));

        Causa causa = new Causa();
        causa.setDescripcion(causaDTO.getDescripcion());
        causa.setArbolDeProblemas(arbolDeProblemas);

        causaRepository.save(causa);

        return mapToDTO(causa);
    }

    @Override
    public List<CausaDTO> obtenerTodos() {
        return causaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CausaDTO obtenerPorId(Long id) {

        Causa causa = causaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la causa con ID: " + id));

        return mapToDTO(causa);
    }

    @Override
    public CausaDTO actualizarCausa(Long id, CausaDTO causaDTO) {

        Causa causa = causaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la causa con ID: " + id));

        causa.setDescripcion(causaDTO.getDescripcion());

        causaRepository.save(causa);

        return mapToDTO(causa);
    }

    @Override
    public boolean eliminarCausa(Long id) {

        Causa causa = causaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la causa con ID: " + id));

        causaRepository.delete(causa);

        return true;
    }

    @Override
    public List<CausaDTO> obtenerCausasPorArbolDeProblemasId(Long arbolDeProblemasId) {

        if (!arbolProblemaRepository.existsById(arbolDeProblemasId)) {
            throw new ResourceNotFoundException(
                    "No existe el árbol de problemas con ID: " + arbolDeProblemasId);
        }

        return causaRepository.findByArbolDeProblemasId(arbolDeProblemasId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private CausaDTO mapToDTO(Causa causa) {
        CausaDTO dto = new CausaDTO();
        dto.setId(causa.getId());
        dto.setDescripcion(causa.getDescripcion());
        dto.setSemaforo(causa.getSemaforo());

        if (causa.getArbolDeProblemas() != null) {
            dto.setArbolDeProblemasId(causa.getArbolDeProblemas().getId());
        }

        return dto;
    }
}