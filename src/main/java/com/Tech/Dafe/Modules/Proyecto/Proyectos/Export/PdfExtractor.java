package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Response.CampoError;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfExtractor {

    /**
     * Obtiene la firma almacenada en el PDF.
     */
    public String extractSignature(String text) {

        for (String line : text.split("\\R")) {

            line = line.trim();

            if (line.startsWith("SIGNATURE:")) {
                return line.substring("SIGNATURE:".length()).trim();
            }
        }

        return "";
    }

    /**
     * Extrae todos los campos del PDF.
     *
     * Este método será utilizado tanto por:
     * - PdfValidationService
     * - parse()
     */
    public Map<String, String> extractFields(String text) {

        Map<String, String> map = new LinkedHashMap<>();

        String[] lines = text.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (line.startsWith("Id:")) {
                map.put("id", value(line));
            }

            else if (line.startsWith("Título:")) {
                map.put("titulo", value(line));
            }

            else if (line.startsWith("Descripcion:")
                    || line.startsWith("Descripción:")) {
                map.put("descripcion", value(line));
            }

            else if (line.startsWith("Población Objetivo:")) {
                map.put("poblacionObjetivo", value(line));
            }

            else if (line.startsWith("Justificación:")
                    || line.startsWith("Justificacion:")) {
                map.put("justificacion", value(line));
            }

            else if (line.startsWith("Presupuesto:")) {
                map.put("presupuesto", value(line));
            }

            else if (line.startsWith("Resultados Esperados:")) {
                map.put("resultadosEsperados", value(line));
            }

            else if (line.startsWith("Observaciones:")) {
                map.put("observaciones", value(line));
            }

            else if (line.startsWith("Estado:")) {
                map.put("estado", value(line));
            }

            else if (line.startsWith("Fecha de Creación:")
                    || line.startsWith("Fecha de Creacion:")) {
                map.put("fechaCreacion", value(line));
            }

            else if (line.startsWith("Fecha de Actualización:")
                    || line.startsWith("Fecha de Actualizacion:")) {
                map.put("fechaActualizacion", value(line));
            }
        }

        return map;
    }

    private String value(String line) {

        int pos = line.indexOf(':');

        if (pos < 0) {
            return "";
        }

        return line.substring(pos + 1).trim();
    }

    public ParsedPdf extract(PDDocument document) {

        try {

            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);

            String signature = extractSignature(text);

            Map<String,String> fields = extractFields(text);

            return new ParsedPdf(
                    text,
                    signature,
                    fields
            );

        }
        catch(IOException ex){
            throw new BusinessException(
                    List.of(
                            new CampoError(
                                    "archivo",
                                    "No fue posible leer el PDF."
                            )
                    )
            );

        }

    }
        
}
