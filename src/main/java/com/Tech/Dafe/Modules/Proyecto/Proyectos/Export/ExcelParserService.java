package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Response.CampoError;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelParserService {

    /**
     * Convierte un Workbook en un Proyecto.
     */
    public Proyecto parse(Workbook workbook) {

        Sheet sheet = workbook.getSheet(ExportConstants.PROJECT_SHEET);

        if (sheet == null) {
            throw error(
                    "No existe la hoja " + ExportConstants.PROJECT_SHEET + "."
            );
        }

        Row row = sheet.getRow(1);

        if (row == null) {
            throw error(
                    "El archivo no contiene proyectos."
            );
        }

        Proyecto proyecto = new Proyecto();

        proyecto.setTitulo(leerTexto(row, 1));
        proyecto.setDescripcion(leerTexto(row, 2));
        proyecto.setPoblacionObjetivo(leerTexto(row, 3));
        proyecto.setJustificacion(leerTexto(row, 4));

        proyecto.setPresupuesto(
                leerDouble(
                        row,
                        5,
                        "presupuesto",
                        "El presupuesto debe contener únicamente valores numéricos."
                )
        );

        proyecto.setResultadosEsperados(leerTexto(row, 6));
        proyecto.setObservaciones(leerTexto(row, 7));
        proyecto.setEstado(leerTexto(row, 8));

        proyecto.setFechaCreacion(
                leerFecha(row, 9)
        );

        proyecto.setFechaActualizacion(
                leerFecha(row, 10)
        );

        return proyecto;
    }

    private String leerTexto(Row row, int columna) {

        Cell cell = row.getCell(columna);

        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();

        String valor = formatter.formatCellValue(cell);

        return valor == null
                ? null
                : valor.trim();
    }

    private Float leerDouble(
            Row row,
            int columna,
            String campo,
            String mensaje
    ) {

        Cell cell = row.getCell(columna);

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (float) cell.getNumericCellValue();
        }

        String valor = leerTexto(row, columna);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return Float.parseFloat(valor);
        } catch (NumberFormatException ex) {
            throw new BusinessException(
                    List.of(
                            new CampoError(
                                    campo,
                                    mensaje
                            )
                    )
            );
        }
    }

    private LocalDateTime leerFecha(
            Row row,
            int columna
    ) {

        Cell cell = row.getCell(columna);

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC
                && DateUtil.isCellDateFormatted(cell)) {

            return cell.getLocalDateTimeCellValue();
        }

        String valor = leerTexto(row, columna);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return DateParser.parse(valor);
    }

    private BusinessException error(String mensaje) {

        return new BusinessException(
                List.of(
                        new CampoError(
                                "archivo",
                                mensaje
                        )
                )
        );
    }

}