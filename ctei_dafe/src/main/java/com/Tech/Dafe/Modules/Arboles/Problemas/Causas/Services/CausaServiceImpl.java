package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Services;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolProblemaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.DTO.CausaDTO;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CausaServiceImpl implements CausaService {

    @Autowired
    private CausaRepository causaRepository;

    @Autowired
    private ArbolProblemaRepository arbolProblemaRepository;

    public CausaDTO crearCausa(CausaDTO causaDTO) {
        ArbolDeProblemas arbolDeProblemas = arbolProblemaRepository.findById(causaDTO.getArbolDeProblemasId())
                .orElseThrow(() -> new IllegalArgumentException("El árbol de objetivos no existe con ID: " + causaDTO.getArbolDeProblemasId()));

        Causa causa = new Causa();
        causa.setCalificacion(causaDTO.getCalificacion());
        causa.setNombre(causaDTO.getNombre());
        causa.setArbolDeProblemas(arbolDeProblemas);

        causaRepository.save(causa);
        return mapToDTO(causa);
    }


    public List<CausaDTO> obtenerTodos() {
        return causaRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CausaDTO obtenerPorId(Long id) {
        return causaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Causa no encontrado con ID: " + id));
    }

    public CausaDTO actualizarMedio(Long id, CausaDTO causaDTO) {
        return causaRepository.findById(id).map(causa -> {
            causa.setCalificacion(causaDTO.getCalificacion());
            causa.setNombre(causaDTO.getNombre());
            causaRepository.save(causa);
            return mapToDTO(causa);
        }).orElseThrow(() -> new IllegalArgumentException("Causa no encontrado con ID: " + id));
    }

    public boolean eliminarMedio(Long id) {
        if (causaRepository.existsById(id)) {
            causaRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Causa no encontrado con ID: " + id);
    }

    public List<CausaDTO> obtenerPorArbolDeObjetivosId(Long arbolDeObjetivosId) {
        List<Causa> causas = causaRepository.findByArbolDeProblemas_Id(arbolDeObjetivosId);

        if (causas.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron Causas para el árbol de objetivos con ID: " + arbolDeObjetivosId);
        }

        return causas.stream().map(this::mapToDTO).toList();
    }

    public CausaDTO actualizarCalificacion(Long id, CausaDTO causaDTO) {
        return causaRepository.findById(id).map(causa -> {
            causa.setCalificacion(causaDTO.getCalificacion());
            causa.setSemaforo(causaDTO.getSemaforo());
            causa.setComentario(causaDTO.getComentario());

            causaRepository.save(causa);
            return mapToDTO(causa);
        }).orElse(null);
    }



    private CausaDTO mapToDTO(Causa causa) {
        CausaDTO dto = new CausaDTO();
        dto.setId(causa.getId());
        dto.setCalificacion(causa.getCalificacion());
        dto.setSemaforo(causa.getSemaforo());
        dto.setComentario(causa.getComentario());
        dto.setNombre(causa.getNombre());
        if (causa.getArbolDeProblemas() != null) {
            dto.setArbolDeProblemasId(causa.getArbolDeProblemas().getId());
        }
        return dto;
    }

}
