package com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Services;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.DTO.FinDTO;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Models.Fin;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Repository.FinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinServiceImpl implements FinService {

    private final FinRepository finRepository;
    private final ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    @Override
    public FinDTO crearFin(FinDTO finDTO) {

        ArbolDeObjetivos arbolDeObjetivos = arbolDeObjetivosRepository.findById(finDTO.getArbolDeObjetivosId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El árbol de objetivos no existe con ID: " + finDTO.getArbolDeObjetivosId()));

        Fin fin = new Fin();
        fin.setDescripcion(finDTO.getDescripcion());
        fin.setArbolDeObjetivos(arbolDeObjetivos);

        return mapToDTO(finRepository.save(fin));
    }

    @Override
    public List<FinDTO> obtenerTodos() {
        return finRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public FinDTO obtenerPorId(Long id) {

        Fin fin = finRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fin no encontrado con ID: " + id));

        return mapToDTO(fin);
    }

    @Override
    public FinDTO actualizarFin(Long id, FinDTO finDTO) {

        Fin fin = finRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fin no encontrado con ID: " + id));

        fin.setDescripcion(finDTO.getDescripcion());

        return mapToDTO(finRepository.save(fin));
    }

    @Override
    public boolean eliminarFin(Long id) {

        Fin fin = finRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fin no encontrado con ID: " + id));

        finRepository.delete(fin);

        return true;
    }

    @Override
    public List<FinDTO> obtenerFinesPorArbolDeObjetivosId(Long arbolDeObjetivosId) {

        List<Fin> fines = finRepository.findByArbolDeObjetivos_Id(arbolDeObjetivosId);

        if (fines.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se encontraron fines para el árbol de objetivos con ID: "
                            + arbolDeObjetivosId);
        }

        return fines.stream()
                .map(this::mapToDTO)
                .toList();
    }


    private FinDTO mapToDTO(Fin fin) {

        FinDTO dto = new FinDTO();

        dto.setId(fin.getId());
        dto.setDescripcion(fin.getDescripcion());
        dto.setSemaforo(fin.getSemaforo());

        if (fin.getArbolDeObjetivos() != null) {
            dto.setArbolDeObjetivosId(fin.getArbolDeObjetivos().getId());
        }

        return dto;
    }

}