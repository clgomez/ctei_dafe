package com.Tech.Dafe.Modules.Proyecto.Proyectos.Services;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO.ProyectoDTO;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Export.PdfExportService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Export.ExcelExportService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Export.ExcelParserService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Export.PdfParserService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Utils.NormalizarTitulo.TituloNormalizer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    
    private final Validator validator;

    private final ExcelExportService excelExportService;

    private final ExcelParserService excelParserService;

    private final PdfExportService pdfExportService;

    private final PdfParserService pdfParserService;



    @Override
    public Iterable<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    @Override
    public Optional<Proyecto> findById(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> obtenerProyectosPorIdUsuario(Long usuarioId) {
        return proyectoRepository.findByUsuarioId(usuarioId);
    }

   /*@Override
    public String normalizarTitulo(String titulo) {

        if (titulo == null) {
            return null;
        }

        return Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // elimina tildes
                .replaceAll("[^a-zA-Z0-9\\s]", "") //ignore signos de puntuación
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "");
                //.replaceAll("\\s+", " ");
    }
    */
    
    @Override
    public Proyecto crearProyecto(Proyecto proyecto) {

        validarProyecto(proyecto);

        validarTituloDuplicado(proyecto.getTitulo(), null);

        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto actualizarProyecto(Long id, ProyectoDTO dto) {

        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el proyecto con ID " + id));

        validarTituloDuplicado(dto.getTitulo(), id);

        proyecto.setTitulo(dto.getTitulo());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setPoblacionObjetivo(dto.getPoblacionObjetivo());
        proyecto.setJustificacion(dto.getJustificacion());
        proyecto.setPresupuesto(dto.getPresupuesto());
        proyecto.setResultadosEsperados(dto.getResultadosEsperados());
        proyecto.setObservaciones(dto.getObservaciones());
        proyecto.setEstado(dto.getEstado());
        proyecto.setFechaActualizacion(LocalDateTime.now());

        //validarProyecto(proyecto);

        return proyectoRepository.save(proyecto);
    }


   private void validarTituloDuplicado(String titulo, Long id) {

    String tituloNormalizado = TituloNormalizer.normalizar(titulo);

    boolean existe;

    if (id == null) {
        // CREACIÓN
        existe = proyectoRepository.existsByTituloNormalizado(tituloNormalizado);

    } else {
        // ACTUALIZACIÓN
        existe = proyectoRepository.existsByTituloNormalizadoAndIdNot(tituloNormalizado, id);
    }

    if (existe) {

        throw new BusinessException(
                List.of(
                        new CampoError(
                                "titulo",
                                "Ya existe un proyecto con este título. Por favor ingrese uno diferente."
                        )
                )
        );
    }
  }

    @Override
    public void validarTituloProyecto(String titulo, Long id) {

        validarTituloDuplicado(titulo, id);
    }


    @Override
    public void validarProyecto(Proyecto proyecto) {

        if (proyecto == null) {
            List<CampoError> errores = new ArrayList<>();
            errores.add(new CampoError("proyecto", "El proyecto no puede ser nulo."));
            throw new BusinessException(errores);
        }

        ProyectoDTO dto = ProyectoDTO.builder()
                .titulo(proyecto.getTitulo())
                .descripcion(proyecto.getDescripcion())
                .poblacionObjetivo(proyecto.getPoblacionObjetivo())
                .justificacion(proyecto.getJustificacion())
                .presupuesto(proyecto.getPresupuesto())
                .resultadosEsperados(proyecto.getResultadosEsperados())
                .observaciones(proyecto.getObservaciones())
                .fechaCreacion(proyecto.getFechaCreacion())
                .fechaActualizacion(proyecto.getFechaActualizacion())
                .estado(proyecto.getEstado())
                .idUsuario(
                    proyecto.getUsuario() != null
                        ? proyecto.getUsuario().getId()
                        : null
                )
                .build();

        Set<ConstraintViolation<ProyectoDTO>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {

            List<CampoError> errores = violations.stream()
                    .map(v -> new CampoError(
                            v.getPropertyPath().toString(),
                            v.getMessage()))
                    .toList();

            throw new BusinessException(errores);
        }
    }


    @Override
    public byte[] exportarProyectoExcel(Long id) {

        try {

            Proyecto proyecto = proyectoRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No existe el proyecto con ID " + id));

            return excelExportService.exportProyecto(proyecto);

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    List.of(
                            new CampoError(
                                    "exportExcel",
                                    "Error generando exportación Excel: " + ex.getMessage()
                            )
                    )
            );
        }
    }

    @Override
    public byte[] exportarProyectoPdf(Long id) {

        try {

            Proyecto proyecto = proyectoRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No existe el proyecto con ID " + id));

            return pdfExportService.exportProyecto(proyecto);

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    List.of(
                            new CampoError(
                                    "exportPdf",
                                    "Error generando exportación PDF: " + ex.getMessage()
                            )
                    )
            );
        }
    }

    @Override
    public Proyecto importarExcel(Workbook workbook) {

        Proyecto proyecto =
                excelParserService.parse(workbook);

        validarProyectoImportado(proyecto);

        return proyecto;
    }

    private void validarProyectoImportado(Proyecto proyecto) {

        List<CampoError> errores = new ArrayList<>();

        if (proyecto.getFechaCreacion() == null) {
            errores.add(new CampoError(
                    "fechaCreacion",
                    "La fecha de creación es obligatoria."
            ));
        }

        if (proyecto.getEstado() == null ||
            !proyecto.getEstado().trim().equalsIgnoreCase("ACTIVO")) {

            errores.add(new CampoError(
                    "estado",
                    "El proyecto solo puede importarse con estado ACTIVO."
            ));
        }

        if (!errores.isEmpty()) {
            throw new BusinessException(errores);
        }
    }


@Override
public Proyecto importarPdf(PDDocument document) {

    Proyecto proyecto = pdfParserService.parse(document);

    validarProyectoImportado(proyecto);

    return proyecto;

}    


}
