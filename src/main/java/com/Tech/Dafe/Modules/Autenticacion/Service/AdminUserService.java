package com.Tech.Dafe.Modules.Autenticacion.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import com.Tech.Dafe.Modules.Usuario.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    
    private final UsuarioService usuarioService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final RolServicio rolService;
    
    private final AutenticacionServicio userService;

   
    @Transactional
    public void createAdminUserIfNotExist() {
        // Verificamos si ya existe un usuario con el rol "ROL_ADMINISTRADOR"
         // Convertir el String nombreRol al Enum RolNombre
            RolNombre rolNombreEnum = RolNombre.valueOf("ROL_ADMINISTRADOR");
            TipoIdentificacionEnum  tipoIdentificacionEnum = TipoIdentificacionEnum.valueOf("CEDULA_CIUDADANIA");
            GeneroEnum generoEnum = GeneroEnum.valueOf("Masculino");
            OcupacionEnum ocupacionEnum = OcupacionEnum.valueOf("Profesor");

        if (usuarioService.findByRoles_RolNombre(rolNombreEnum).isEmpty()) {
            
            Usuario adminUser = new Usuario(
                    "admin_nombre",
                    "admin_apellidos",
                    "admin_direccion",
                    "admin@gmail.com",
                    "ACTIVO",
                    LocalDate.of(2000, 1, 1),
                    tipoIdentificacionEnum,
                    "1060000000",
                    generoEnum,
                    ocupacionEnum,
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "3205551111"
            );

         

            //adminUser.setFechaRegistro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            adminUser.setFechaRegistro(LocalDateTime.now());

            Set<Rol> roles = new HashSet<>();
            roles.add(rolService.getByRolName(RolNombre.ROL_ADMINISTRADOR).get());

            adminUser.setRoles(roles);
            userService.save(adminUser);
          
           
        }
    }
}

