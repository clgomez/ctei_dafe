package com.Tech.Dafe.Modules.Autenticacion.Util;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Service.RolServicio;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class CreateRoles implements CommandLineRunner {

    private final RolServicio rolService;
    
    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound(RolNombre.ROL_INVESTIGADOR);
        createRoleIfNotFound(RolNombre.ROL_TUTOR);
        createRoleIfNotFound(RolNombre.ROL_EVALUADOR);
        createRoleIfNotFound(RolNombre.ROL_ADMINISTRADOR);

        
    }

    private void createRoleIfNotFound(RolNombre rolNombre) {
        if (rolService.getByRolName(rolNombre).isEmpty()) {
            Rol rol = new Rol(rolNombre);
            rolService.save(rol);
        }
    }
}