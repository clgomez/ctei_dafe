/*
package com.Tech.Dafe.Modules.Actividades.Cronograma.Service;

import com.Tech.Dafe.Modules.Actividades.Actividades.Repository.ActividadRepository;
import com.Tech.Dafe.Modules.Actividades.Cronograma.DTO.CronogramaDTO;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Repository.CronogramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CronogramaServiceImpl implements CronogramaService{

    private final CronogramaRepository cronogramaRepository;
    
    private final ActividadRepository actividadRepository;

    public CronogramaDTO crearCronograma(CronogramaDTO cronogramaDTO) {
        Cronograma cronograma = new Cronograma();
        cronograma.setFechaInicio(cronogramaDTO.getFechaInicio());
        cronograma.setFechaFin(cronogramaDTO.getFechaFin());
        cronograma.setActividad(actividadRepository.findById(cronogramaDTO.getActividadId()).orElse(null));

        cronograma = cronogramaRepository.save(cronograma);
        cronogramaDTO.setId(cronograma.getId());
        return cronogramaDTO;
    }

    public List<CronogramaDTO> obtenerTodos() {
        return cronogramaRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<CronogramaDTO> obtenerPorId(Long id) {
        return cronogramaRepository.findById(id).map(this::mapToDTO);
    }

    public CronogramaDTO actualizarCronograma(Long id, CronogramaDTO cronogramaDTO) {
       return cronogramaRepository.findById(id).map(cronograma -> {
            cronograma.setFechaInicio(cronogramaDTO.getFechaInicio());
            cronograma.setFechaFin(cronogramaDTO.getFechaFin());
            cronograma.setActividad(actividadRepository.findById(cronogramaDTO.getActividadId()).orElse(null));

            cronogramaRepository.save(cronograma);
            return mapToDTO(cronograma);
        }).orElse(null);
    }

    public boolean eliminarCronograma(Long id) {
        if (cronogramaRepository.existsById(id)) {
            cronogramaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CronogramaDTO> obtenerCronogramaPorIdActividad(Long actividadId) {
       return cronogramaRepository.findByActividadId(actividadId).map(this::mapToDTO);
    }

    private CronogramaDTO mapToDTO(Cronograma cronograma) {
        CronogramaDTO dto = new CronogramaDTO();
        dto.setId(cronograma.getId());
        dto.setFechaInicio(cronograma.getFechaInicio());
        dto.setFechaFin(cronograma.getFechaFin());
        dto.setActividadId(cronograma.getActividad().getId());
        return dto;
    }
}
*/

package com.Tech.Dafe.Modules.Actividades.Cronograma.Service;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Actividades.Actividades.Repository.ActividadRepository;
import com.Tech.Dafe.Modules.Actividades.Cronograma.DTO.CronogramaDTO;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Repository.CronogramaRepository;
import com.Tech.Dafe.Response.CampoError;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaServiceImpl implements CronogramaService {

    private final CronogramaRepository cronogramaRepository;
    private final ActividadRepository actividadRepository;

       private void validarFechas(CronogramaDTO dto) {

        List<CampoError> errores = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        if (dto.getFechaInicio().isBefore(hoy)) {
            errores.add(new CampoError(
                    "fechaInicio",
                    "La fecha de inicio no puede ser anterior a hoy"));
        }

        if (!dto.getFechaFin().isAfter(hoy)) {
            errores.add(new CampoError(
                    "fechaFin",
                    "La fecha final debe ser posterior a hoy"));
        }

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            errores.add(new CampoError(
                    "fechaFin",
                    "La fecha final debe ser mayor que la fecha de inicio"));
        }

        if (!errores.isEmpty()) {
            throw new BusinessException(errores);
        }
    }


    @Override
    public CronogramaDTO crearCronograma(CronogramaDTO cronogramaDTO) {

        var actividad = actividadRepository.findById(cronogramaDTO.getActividadId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una actividad con id " + cronogramaDTO.getActividadId()));


        validarFechas(cronogramaDTO);                        

        Cronograma cronograma = new Cronograma();
        cronograma.setFechaInicio(cronogramaDTO.getFechaInicio());
        cronograma.setFechaFin(cronogramaDTO.getFechaFin());
        cronograma.setActividad(actividad);

        cronograma = cronogramaRepository.save(cronograma);

        return mapToDTO(cronograma);
    }

    @Override
    public List<CronogramaDTO> obtenerTodos() {
        return cronogramaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CronogramaDTO obtenerPorId(Long id) {

        return cronogramaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un cronograma con id " + id));
    }

    @Override
    public CronogramaDTO actualizarCronograma(Long id, CronogramaDTO cronogramaDTO) {

        Cronograma cronograma = cronogramaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un cronograma con id " + id));

        var actividad = actividadRepository.findById(cronogramaDTO.getActividadId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una actividad con id " + cronogramaDTO.getActividadId()));

        validarFechas(cronogramaDTO);                            

        cronograma.setFechaInicio(cronogramaDTO.getFechaInicio());
        cronograma.setFechaFin(cronogramaDTO.getFechaFin());
        cronograma.setActividad(actividad);

        return mapToDTO(cronogramaRepository.save(cronograma));
    }

    @Override
    public void eliminarCronograma(Long id) {

        if (!cronogramaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe un cronograma con id " + id);
        }

        cronogramaRepository.deleteById(id);
    }

    @Override
    public CronogramaDTO obtenerCronogramaPorIdActividad(Long actividadId) {

        return cronogramaRepository.findByActividadId(actividadId)
                .map(this::mapToDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un cronograma para la actividad con id " + actividadId));
    }

    private CronogramaDTO mapToDTO(Cronograma cronograma) {

        CronogramaDTO dto = new CronogramaDTO();
        dto.setId(cronograma.getId());
        dto.setFechaInicio(cronograma.getFechaInicio());
        dto.setFechaFin(cronograma.getFechaFin());
        dto.setActividadId(cronograma.getActividad().getId());

        return dto;
    }
}