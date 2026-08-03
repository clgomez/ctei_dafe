package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Service;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO.ConvocatoriaDTO;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Repository.ConvocatoriaRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.DTO.InscripcionDTO;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Service.InscripcionService;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Utils.NormalizarTitulo.TituloNormalizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConvocatoriaServiceImpl implements ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final NotificacionService notificacionService;
    private final InscripcionService inscripcionService;

    @Override
    public List<Convocatoria> obtenerConvocatorias() {
        return convocatoriaRepository.findAll();
    }

    @Override
    public Convocatoria obtenerConvocatoriaPorId(Long id) {

        return convocatoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una convocatoria con id " + id));
    }

    private void validarFechas(ConvocatoriaDTO dto) {

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
    public Convocatoria crearConvocatoria(ConvocatoriaDTO convocatoriaDTO) {

        validarFechas(convocatoriaDTO);

        validarTituloDuplicado(convocatoriaDTO.getTitulo(), null);

        Convocatoria convocatoria = new Convocatoria();
        convocatoria.setTitulo(convocatoriaDTO.getTitulo());
        convocatoria.setDescripcion(convocatoriaDTO.getDescripcion());
        convocatoria.setEstado(convocatoriaDTO.getEstado());
        convocatoria.setFechaInicio(convocatoriaDTO.getFechaInicio());
        convocatoria.setFechaFin(convocatoriaDTO.getFechaFin());

        Convocatoria nuevaConvocatoria = convocatoriaRepository.save(convocatoria);

        notificacionService.enviarNotificacionesPorRol(
                RolNombre.ROL_INVESTIGADOR,
                "Nueva convocatoria creada: " + nuevaConvocatoria.getDescripcion());

        return nuevaConvocatoria;
    }

   

    @Override
    public Convocatoria actualizarConvocatoria(Long id, ConvocatoriaDTO convocatoriaDTO) {

        validarFechas(convocatoriaDTO);

        Convocatoria convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una convocatoria con id " + id));

        validarTituloDuplicado(convocatoriaDTO.getTitulo(), id);                        
        
        convocatoria.setTitulo(convocatoriaDTO.getTitulo());
        convocatoria.setDescripcion(convocatoriaDTO.getDescripcion());
        convocatoria.setEstado(convocatoriaDTO.getEstado());
        convocatoria.setFechaInicio(convocatoriaDTO.getFechaInicio());
        convocatoria.setFechaFin(convocatoriaDTO.getFechaFin());

        Convocatoria convocatoriaActualizada = convocatoriaRepository.save(convocatoria);

        notificacionService.enviarNotificacionesPorRol(
                RolNombre.ROL_INVESTIGADOR,
                "Convocatoria actualizada: " + convocatoriaActualizada.getDescripcion());

        return convocatoriaActualizada;
    }

    @Override
    public void eliminarConvocatoria(Long id) {

        if (!convocatoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe una convocatoria con id " + id);
        }

        convocatoriaRepository.deleteById(id);
    }

    @Override
    public Optional<Convocatoria> obtenerConvocatoriaPorProyectoIdYUsuarioInvestigadorId(
                Long proyectoId,
                Long usuarioInvestigadorId) {

        InscripcionDTO inscripcion =
                inscripcionService.obtenerInscripcionPorIdProyectoYIdUsuarioInvestigador(
                        proyectoId,
                        usuarioInvestigadorId);

        Convocatoria convocatoria = convocatoriaRepository
                .findById(inscripcion.getConvocatoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la convocatoria asociada a la inscripción."));

        return Optional.of(convocatoria);
    }


    private void validarTituloDuplicado(String titulo, Long id) {

        String tituloNormalizado = TituloNormalizer.normalizar(titulo);

        boolean existe;

        if (id == null) {
            // CREACIÓN
            existe = convocatoriaRepository.existsByTituloNormalizado(tituloNormalizado);

        } else {
            // ACTUALIZACIÓN
            existe = convocatoriaRepository.existsByTituloNormalizadoAndIdNot(tituloNormalizado, id);
        }

        if (existe) {

            throw new BusinessException(
                    List.of(
                            new CampoError(
                                    "titulo",
                                    "Ya existe una convocatoria con este título. Por favor ingrese una diferente."
                            )
                    )
            );
        }
  }

    @Override
    public void validarTituloConvocatoria(String titulo, Long id) {

        validarTituloDuplicado(titulo, id);
    }
}