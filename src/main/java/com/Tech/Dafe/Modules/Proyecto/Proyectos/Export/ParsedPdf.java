package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ParsedPdf {

    private final String text;

    private final String signature;

    private final Map<String,String> fields;

}