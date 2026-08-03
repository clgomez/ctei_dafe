package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportMetadata {

    private String sistema;
    private String version;
    private String tipo;
    private String formato;
    private String modulo;
    private String exportadoPor;
    private String fechaExportacion;
    private String firma;

}