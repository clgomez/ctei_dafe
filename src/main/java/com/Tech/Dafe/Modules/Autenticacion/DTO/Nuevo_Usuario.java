package com.Tech.Dafe.Modules.Autenticacion.DTO;

import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nuevo_Usuario {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String direccion;

    @Email
    private String email;

    @NotBlank
    private String estado;

    @NotBlank
    private String fecha_nacimiento;

    @NotNull
    private TipoIdentificacionEnum tipoIdentificacion;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<String> roles = new HashSet<>();

    @NotBlank
    private String telefono;

    @NotNull
    private GeneroEnum genero;

    @NotNull
    private OcupacionEnum ocupacion;
}
