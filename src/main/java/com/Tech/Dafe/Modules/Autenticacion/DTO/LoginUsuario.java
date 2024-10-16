package com.Tech.Dafe.Modules.Autenticacion.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuario {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
