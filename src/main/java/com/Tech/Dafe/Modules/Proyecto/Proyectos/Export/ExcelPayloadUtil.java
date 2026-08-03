package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExcelPayloadUtil {

    private ExcelPayloadUtil() {
    }

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Serializa un Proyecto exactamente igual para exportación e importación.
     */
    public static String serialize(Proyecto proyecto) {

        StringBuilder sb = new StringBuilder();

        sb.append(n(proyecto.getId())).append('|');
        sb.append(n(proyecto.getTitulo())).append('|');
        sb.append(n(proyecto.getDescripcion())).append('|');
        sb.append(n(proyecto.getPoblacionObjetivo())).append('|');
        sb.append(n(proyecto.getJustificacion())).append('|');
        sb.append(n(proyecto.getPresupuesto())).append('|');
        sb.append(n(proyecto.getResultadosEsperados())).append('|');
        sb.append(n(proyecto.getObservaciones())).append('|');
        sb.append(n(proyecto.getEstado())).append('|');
        sb.append(formatDate(proyecto.getFechaCreacion())).append('|');
        sb.append(formatDate(proyecto.getFechaActualizacion()));

        return sb.toString();
    }

    /**
     * Reconstruye el payload leyendo directamente desde la fila del Excel.
     */
    public static String serialize(Row row) {

        StringBuilder sb = new StringBuilder();

        sb.append(idValue(row.getCell(0))).append('|');
        sb.append(value(row.getCell(1))).append('|');
        sb.append(value(row.getCell(2))).append('|');
        sb.append(value(row.getCell(3))).append('|');
        sb.append(value(row.getCell(4))).append('|');
        sb.append(value(row.getCell(5))).append('|');
        sb.append(value(row.getCell(6))).append('|');
        sb.append(value(row.getCell(7))).append('|');
        sb.append(value(row.getCell(8))).append('|');
        sb.append(value(row.getCell(9))).append('|');
        sb.append(value(row.getCell(10)));

        return sb.toString();
    }

    private static String idValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {

            case NUMERIC:
                return Long.toString((long) cell.getNumericCellValue());

            case STRING:
                return cell.getStringCellValue().trim();

            default:
                return value(cell);
        }
    }

    public static String formatDate(LocalDateTime fecha) {

        return fecha == null
                ? ""
                : fecha.format(DATE_FORMAT);

    }

    private static String value(Cell cell) {

        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                        return formatDate(cell.getLocalDateTimeCellValue());
                    }

                return Double.toString(cell.getNumericCellValue());
       

            case BOOLEAN:
                return Boolean.toString(
                        cell.getBooleanCellValue()
                );

            case FORMULA:

                FormulaEvaluator evaluator =
                        cell.getSheet()
                                .getWorkbook()
                                .getCreationHelper()
                                .createFormulaEvaluator();

                CellValue result =
                        evaluator.evaluate(cell);

                switch (result.getCellType()) {

                    case STRING:
                        return result.getStringValue().trim();

                    case NUMERIC:
                        return Float.toString(
                                (float) result.getNumberValue()
                        );

                    case BOOLEAN:
                        return Boolean.toString(
                                result.getBooleanValue()
                        );

                    default:
                        return "";
                }

            default:
                return "";
        }

    }

    private static String n(Object valor) {

        if (valor == null) {
            return "";
        }

        return valor.toString()
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();

    }

}