package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Response.CampoError;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelValidationService {

    private static final String[] COLUMNAS = {
            "ID",
            "TITULO",
            "DESCRIPCION",
            "POBLACION OBJETIVO",
            "JUSTIFICACION",
            "PRESUPUESTO",
            "RESULTADOS ESPERADOS",
            "OBSERVACIONES",
            "ESTADO",
            "FECHA DE CREACION",
            "FECHA DE ACTUALIZACION"
    };

    private final FileSignatureService signatureService;

    /**
     * Punto único de validación.
     */
    public void validate(Workbook workbook) {

        validarNumeroHojas(workbook);

        validarMetadata(workbook);

        validarCabecera(workbook);

        validarCantidadRegistros(workbook);

        validarFirma(workbook);
    }

    // ==========================================================
    // ESTRUCTURA
    // ==========================================================

    private void validarNumeroHojas(Workbook workbook) {

        if (workbook.getNumberOfSheets() != 2) {

            throw error(
                    "El archivo Excel tiene una estructura inválida."
            );

        }

    }

    // ==========================================================
    // METADATA
    // ==========================================================

    private void validarMetadata(Workbook workbook) {

        Sheet metadata =
                workbook.getSheet(ExportConstants.METADATA_SHEET);

        if (metadata == null) {

            throw error(
                    "El archivo Excel no fue generado por DAFE."
            );

        }

        validarValor(metadata, 0, "DAFE");
        validarValor(metadata, 1, "2");
        validarValor(metadata, 2, "PROYECTO");
        validarValor(metadata, 3, "EXPORT_V2");

    }

    private void validarValor(
            Sheet metadata,
            int fila,
            String esperado
    ) {

        Row row = metadata.getRow(fila);

        if (row == null) {

            throw error(
                    "La metadata del archivo está incompleta."
            );

        }

        Cell cell = row.getCell(1);

        if (cell == null) {

            throw error(
                    "La metadata del archivo está incompleta."
            );

        }

        String valor = cell.getStringCellValue().trim();

        if (!esperado.equals(valor)) {

            throw error(
                    "El archivo Excel no pertenece al sistema DAFE."
            );

        }

    }

    // ==========================================================
    // CABECERA
    // ==========================================================

    private void validarCabecera(Workbook workbook) {

        Sheet sheet =
                workbook.getSheet(ExportConstants.PROJECT_SHEET);

        if (sheet == null) {

            throw error(
                    "No existe la hoja Proyecto."
            );

        }

        Row header = sheet.getRow(0);

        if (header == null) {

            throw error(
                    "El archivo no contiene cabecera."
            );

        }

        if (header.getPhysicalNumberOfCells() != COLUMNAS.length) {

            throw error(
                    "Cantidad de columnas incorrecta."
            );

        }

        for (int i = 0; i < COLUMNAS.length; i++) {

            Cell cell = header.getCell(i);

            if (cell == null) {

                throw error(
                        "La columna " + (i + 1) + " debe llamarse " + COLUMNAS[i]
                );

            }

            String nombre = cell.getStringCellValue().trim();

            if (!COLUMNAS[i].equalsIgnoreCase(nombre)) {

                throw error(
                        "La columna " + (i + 1) + " debe llamarse " + COLUMNAS[i]
                );

            }

        }

    }

    // ==========================================================
    // REGISTROS
    // ==========================================================

    private void validarCantidadRegistros(Workbook workbook) {

        Sheet sheet =
                workbook.getSheet(ExportConstants.PROJECT_SHEET);

        if (sheet.getLastRowNum() == 0) {

            throw error(
                    "El archivo no contiene proyectos."
            );

        }

        if (sheet.getLastRowNum() > 1) {

            throw error(
                    "Solo se permite importar un proyecto por archivo."
            );

        }

    }

    // ==========================================================
    // FIRMA DIGITAL
    // ==========================================================


   private void validarFirma(Workbook workbook) {

    Sheet meta =
            workbook.getSheet(
                    ExportConstants.METADATA_SHEET
            );

    String signature =
            getCell(meta, "SIGNATURE");

    Sheet sheet =
            workbook.getSheet(
                    ExportConstants.PROJECT_SHEET
            );

    Row row = sheet.getRow(1);

    String payload =
            ExcelPayloadUtil.serialize(row);

     System.out.println("========== PAYLOAD IMPORT ==========");
     System.out.println(payload);

     System.out.println("========== SIGNATURE ==========");
     System.out.println(signature);

     System.out.println("========== GENERATED ==========");
     System.out.println(signatureService.generateSignature(payload));       

    boolean valid =
            signatureService.verifySignature(
                    payload,
                    signature
            );

    if (!valid) {

        throw error(
                "Archivo modificado o corrupto (firma inválida)"
        );

    }

}
     

    // ==========================================================
    // METADATA
    // ==========================================================

    private String getCell(
            Sheet sheet,
            String key
    ) {

        for (Row row : sheet) {

            Cell k = row.getCell(0);

            if (k != null && key.equals(k.toString())) {

                Cell v = row.getCell(1);

                return v == null
                        ? ""
                        : v.toString();

            }

        }

        throw error(
                "Metadata no encontrada: " + key
        );

    }

    // ==========================================================
    // HELPER
    // ==========================================================

    private BusinessException error(String mensaje) {

        List<CampoError> errores = new ArrayList<>();

        errores.add(
                new CampoError(
                        "archivo",
                        mensaje
                )
        );

        return new BusinessException(errores);

    }

}