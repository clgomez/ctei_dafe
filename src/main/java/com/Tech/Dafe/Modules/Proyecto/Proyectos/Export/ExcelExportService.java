package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final FileSignatureService signatureService;

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String formatDate(LocalDateTime fecha) {
        return fecha == null ? "" : fecha.format(DATE_FORMAT);
    }

    @Value("${dafe.export.system}")
    private String sistema;

    @Value("${dafe.export.version}")
    private String version;

    @Value("${dafe.export.module}")
    private String modulo;

    @Value("${dafe.export.type}")
    private String tipo;

    @Value("${dafe.export.format}")
    private String formato;

    public byte[] exportProyecto(Proyecto proyecto) {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet(
                            ExportConstants.PROJECT_SHEET);

            createHeader(sheet);

            createProject(sheet, proyecto);

            String serialized =
                    ExcelPayloadUtil.serialize(proyecto);

            System.out.println("========== PAYLOAD EXPORT ==========");
            System.out.println(serialized);        

            String signature =
                    signatureService.generateSignature(serialized);

            createMetadata(
                    workbook,
                    signature
            );

            workbook.setSheetHidden(
                    workbook.getSheetIndex(
                            ExportConstants.METADATA_SHEET
                    ),
                    true
            );

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);

            workbook.close();

            return out.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error exportando Excel",
                    ex
            );

        }

    }

    private void createHeader(Sheet sheet){

        Row row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("TITULO");
        row.createCell(2).setCellValue("DESCRIPCION");
        row.createCell(3).setCellValue("POBLACION OBJETIVO");
        row.createCell(4).setCellValue("JUSTIFICACION");
        row.createCell(5).setCellValue("PRESUPUESTO");
        row.createCell(6).setCellValue("RESULTADOS ESPERADOS");
        row.createCell(7).setCellValue("OBSERVACIONES");
        row.createCell(8).setCellValue("ESTADO");
        row.createCell(9).setCellValue("FECHA DE CREACION");
        row.createCell(10).setCellValue("FECHA DE ACTUALIZACION");

    }

    private void createProject(
            Sheet sheet,
            Proyecto p){

        Row row = sheet.createRow(1);

        row.createCell(0).setCellValue(p.getId());

        row.createCell(1).setCellValue(p.getTitulo());

        row.createCell(2).setCellValue(p.getDescripcion());

        row.createCell(3).setCellValue(p.getPoblacionObjetivo());

        row.createCell(4).setCellValue(p.getJustificacion());

        row.createCell(5).setCellValue(p.getPresupuesto());

        row.createCell(6).setCellValue(p.getResultadosEsperados());

        row.createCell(7).setCellValue(p.getObservaciones());

        row.createCell(8).setCellValue(p.getEstado());

        row.createCell(9).setCellValue(formatDate(p.getFechaCreacion()));
        
        row.createCell(10).setCellValue(formatDate(p.getFechaActualizacion()));

    }

    private void createMetadata(
            Workbook workbook,
            String signature){

        Sheet meta =
                workbook.createSheet(
                        ExportConstants.METADATA_SHEET);

        int r=0;

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("SISTEMA");

        meta.getRow(0)
                .createCell(1)
                .setCellValue(sistema);

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("VERSION");

        meta.getRow(1)
                .createCell(1)
                .setCellValue(version);

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("TIPO");

        meta.getRow(2)
                .createCell(1)
                .setCellValue(tipo);

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("FORMATO");

        meta.getRow(3)
                .createCell(1)
                .setCellValue(formato);

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("MODULO");

        meta.getRow(4)
                .createCell(1)
                .setCellValue(modulo);

        meta.createRow(r++)
                .createCell(0)
                .setCellValue("FECHA_EXPORTACION");

        meta.getRow(5)
                .createCell(1)
                .setCellValue(formatDate(LocalDateTime.now()));

        meta.createRow(r)
                .createCell(0)
                .setCellValue("SIGNATURE");

        meta.getRow(r)
                .createCell(1)
                .setCellValue(signature);

    }
 
}