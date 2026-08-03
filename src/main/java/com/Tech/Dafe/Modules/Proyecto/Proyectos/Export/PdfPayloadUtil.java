package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class PdfPayloadUtil {

    private PdfPayloadUtil() {
    }

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Serializa un Proyecto.
     * Utilizado durante la exportación.
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
     * Reconstruye exactamente el mismo payload
     * leyendo los valores extraídos del PDF.
     *
     * Utilizado durante la importación.
     */
    public static String serialize(Map<String, String> values) {

        StringBuilder sb = new StringBuilder();

        sb.append(n(values.get("id"))).append('|');
        sb.append(n(values.get("titulo"))).append('|');
        sb.append(n(values.get("descripcion"))).append('|');
        sb.append(n(values.get("poblacionObjetivo"))).append('|');
        sb.append(n(values.get("justificacion"))).append('|');
        sb.append(n(values.get("presupuesto"))).append('|');
        sb.append(n(values.get("resultadosEsperados"))).append('|');
        sb.append(n(values.get("observaciones"))).append('|');
        sb.append(n(values.get("estado"))).append('|');
        sb.append(n(values.get("fechaCreacion"))).append('|');
        sb.append(n(values.get("fechaActualizacion")));

        return sb.toString();
    }

    public static String formatDate(LocalDateTime fecha) {
        return fecha == null
                ? ""
                : fecha.format(DATE_FORMAT);
    }

    /**
     * Normaliza cualquier valor para que el payload
     * sea idéntico entre exportación e importación.
     */
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