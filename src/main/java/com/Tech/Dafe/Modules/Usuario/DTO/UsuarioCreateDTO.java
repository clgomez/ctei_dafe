package com.Tech.Dafe.Modules.Usuario.DTO;

import com.Tech.Dafe.Modules.Autenticacion.Validation.EdadMaxima;
import com.Tech.Dafe.Modules.Autenticacion.Validation.EdadMinima;
import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
        regexp = "^$|^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$",
        message = "El nombre debe contener solo letras y tener entre 2 y 50 caracteres"
    )
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Pattern(
        regexp = "^$|^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$",
        message = "Los apellidos deben contener solo letras y tener entre 2 y 50 caracteres"
    )
    private String apellidos;

    
    @NotBlank(message = "La dirección es obligatoria")
    //@size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    @Pattern(
        regexp = "^$|^.{5,100}$",
        message = "La dirección debe tener entre 5 y 100 caracteres"
    )
    private String direccion;

    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(
        regexp = "^$|^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$",
        message = "Formato de email inválido. Ejemplo válido: usuario@dominio.com")
    private String email;


    @NotBlank(message = "El estado del usuario es obligatorio")
    @Pattern(
        regexp = "^$|^(ACTIVO|NO ACTIVO)$",
        message = "El estado del usuario solo puede ser ACTIVO o NO ACTIVO"
    )
    private String estado;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @EdadMinima(min = 9, message = "Debes tener al menos 9 años para registrarte")
    @EdadMaxima(max = 80, message = "La edad no puede ser mayor a 80 años para registrarse")
    private LocalDate fechaNacimiento;
    
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacionEnum tipoIdentificacion;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(
        regexp = "^$|^\\d{6,15}$",
        message = "La identificación debe contener entre 6 y 15 dígitos"
    )
    private String identificacion;

    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Pattern(
        regexp = "^$|^[A-Za-z0-9_]{4,20}$",
        message = "El nombre de usuario debe tener entre 4 y 20 caracteres y solo puede contener letras, números o guión bajo"
    )
    private String username;


    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
        message = "La contraseña debe tener mínimo 8 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial"
    )
    private String password;


    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
        regexp = "^$|^\\d{10}$",
        message = "El teléfono debe contener exactamente 10 dígitos"
    )
    private String telefono;

    @NotNull(message = "El género es obligatorio")
    private GeneroEnum genero;

    @NotNull(message = "La ocupación es obligatoria")
    private OcupacionEnum ocupacion;
  
    private Set<RolNombre> roles;
  

    public UsuarioCreateDTO(Long id, String nombre) {
        this.nombre = nombre;
    }

}
