package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Response.CampoError;

import lombok.RequiredArgsConstructor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfParserService {

    private final PdfValidationService pdfValidationService;

    private final PdfExtractor pdfExtractor;

    /**
     * Convierte el texto del PDF en un Proyecto.
    */
    public Proyecto parse(PDDocument document) {

        ParsedPdf pdf = pdfExtractor.extract(document);    

        pdfValidationService.validate(pdf);

        Map<String, String> values = pdf.getFields();

        Proyecto proyecto = new Proyecto();

        proyecto.setTitulo(values.get("titulo"));
        proyecto.setDescripcion(values.get("descripcion"));
        proyecto.setPoblacionObjetivo(values.get("poblacionObjetivo"));
        proyecto.setJustificacion(values.get("justificacion"));
        proyecto.setResultadosEsperados(values.get("resultadosEsperados"));
        proyecto.setObservaciones(values.get("observaciones"));
        proyecto.setEstado(values.get("estado"));

        if (!isBlank(values.get("presupuesto"))) {
            try {
                proyecto.setPresupuesto(
                        Float.parseFloat(values.get("presupuesto"))
                );
            } catch (NumberFormatException ex) {
                throw error("Presupuesto inválido.");
            }
        }

        if (!isBlank(values.get("fechaCreacion"))) {
            proyecto.setFechaCreacion(
                    DateParser.parse(values.get("fechaCreacion"))
            );
        }

        if (!isBlank(values.get("fechaActualizacion"))) {
            proyecto.setFechaActualizacion(
                    DateParser.parse(values.get("fechaActualizacion"))
            );
        }

        return proyecto;
    }

    

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BusinessException error(String message) {

        return new BusinessException(
                List.of(
                        new CampoError(
                                "archivo",
                                message
                        )
                )
        );
    }
}