package com.Tech.Dafe.Utils.NormalizarTitulo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidarTituloDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;
}