package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final FileSignatureService signatureService;

    public byte[] exportProyecto(Proyecto p) {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content =
                    new PDPageContentStream(document, page);

            // ==========================
            // ENCABEZADO DAFE
            // ==========================
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(50, 750);
            content.showText("DAFE - REPORTE DE PROYECTO");
            content.endText();

            // ==========================
            // CONTENIDO
            // ==========================
            int y = 720;

            y = writeLine(content, "Id: " + p.getId(), y);
            y = writeLine(content, "Título: " + p.getTitulo(), y);
            y = writeLine(content, "Descripción: " + p.getDescripcion(), y);
            y = writeLine(content, "Población Objetivo: " + p.getPoblacionObjetivo(), y);
            y = writeLine(content, "Justificación: " + p.getJustificacion(), y);
            y = writeLine(content, "Presupuesto: " + p.getPresupuesto(), y);
            y = writeLine(content, "Resultados Esperados: " + p.getResultadosEsperados(), y);
            y = writeLine(content, "Observaciones: " + p.getObservaciones(), y);
            y = writeLine(content, "Estado: " + p.getEstado(), y);
            y = writeLine(content, "Fecha de Creación: " + PdfPayloadUtil.formatDate(p.getFechaCreacion()), y);
            y = writeLine(content, "Fecha de Actualización: " + PdfPayloadUtil.formatDate(p.getFechaActualizacion()), y);

            // ==========================
            // METADATA (HMAC INPUT)
            // ==========================
            
            //String payload = serialize(p);
            String payload = PdfPayloadUtil.serialize(p);

            String signature =
                    signatureService.generateSignature(payload);

            y -= 20;

            y = writeLine(content, "FECHA EXPORTACIÓN: " + PdfPayloadUtil.formatDate(LocalDateTime.now()), y);
            y = writeLine(content, "SIGNATURE: " + signature, y);

            content.close();

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            document.save(out);

            return out.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error generando PDF",
                    ex
            );

        }
    }

    // ==========================
    // ESCRITURA LÍNEA POR LÍNEA
    // ==========================
    private int writeLine(
            PDPageContentStream content,
            String text,
            int y
    ) throws Exception {

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 10);
        content.newLineAtOffset(50, y);
        content.showText(
                safe(text)
        );
        content.endText();

        return y - 20;
    }


    private String safe(String value){

        if(value==null){
            return "";
        }

        return value
                .replace("\r"," ")
                .replace("\n"," ")
                .trim();

    }    
}