package com.Tech.Dafe.Utils.NormalizarTitulo;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TituloNormalizer {

    private static final Set<String> STOPWORDS = Set.of(
            "de", "del", "la", "las", "el", "los",
            "para", "por", "en", "y", "e", "o", "u"
    );

    private TituloNormalizer() {
    }

    public static String normalizar(String titulo) {

        if (titulo == null) {
            return null;
        }

        // Separar CamelCase
        titulo = titulo.replaceAll(
                "([a-z])([A-Z])",
                "$1 $2"
        );

        // Eliminar tildes
        titulo = Normalizer.normalize(
                titulo,
                Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");


        // Convertir a minúsculas
        titulo = titulo.toLowerCase();

         // Eliminar signos de puntuación
        titulo = titulo.replaceAll(
                "[^a-z0-9\\s]",
                " "
        );

        // Eliminar espacios repetidos
        titulo = titulo.trim()
                .replaceAll("\\s+", " ");

        // Eliminar stopwords
        return Arrays.stream(titulo.split(" "))
                .filter(palabra -> !STOPWORDS.contains(palabra))
                .collect(Collectors.joining(""));
    }
}