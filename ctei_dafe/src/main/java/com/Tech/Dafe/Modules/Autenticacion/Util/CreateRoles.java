package com.Tech.Dafe.Modules.Autenticacion.Util;


import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Service.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolServicio rolService;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound(RolNombre.ROL_INVESTIGADOR);
        createRoleIfNotFound(RolNombre.ROL_TUTOR);
        createRoleIfNotFound(RolNombre.ROL_EVALUADOR);
        createRoleIfNotFound(RolNombre.ROL_ADMINISTRADOR);
    }

    private void createRoleIfNotFound(RolNombre rolNombre) {
        if (!rolService.getByRolName(rolNombre).isPresent()) {
            Rol rol = new Rol(rolNombre);
            rolService.save(rol);
        }
    }
}