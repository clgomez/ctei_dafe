package com.Tech.Dafe.Email.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
        message = "La contraseña debe tener mínimo 8 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial"
    )
    private String password;

    @NotBlank(message = "La contraseña de confirmación es obligatoria")
    private String confirmarPassword;

    @NotBlank(message = "El token password es obligatorio")
    private String tokenPassword;
}
