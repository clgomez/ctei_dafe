package com.Tech.Dafe.Modules.Usuario.DTO;

import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String direccion;

    @Email
    @NotBlank
    private String email;

    private String estado;
    private String fecha_nacimiento;
    private TipoIdentificacionEnum tipoIdentificacion;
    private String identificacion;
    private GeneroEnum genero;
    private OcupacionEnum ocupacion;
    private String username;
    private String password;
    private String telefono;

    private Set<RolNombre> roles;

    public UsuarioDTO(Long id, String nombre) {
        this.nombre = nombre;
    }

}
