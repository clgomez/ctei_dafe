package com.Tech.Dafe.Modules.Autenticacion.Util;

import com.Tech.Dafe.Modules.Autenticacion.Service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//@Order(2)
public class CrearAdministrador implements CommandLineRunner {
    
    private final AdminUserService adminUserService;

    @Override
    public void run(String... args) throws Exception {
        // Crear el usuario administrador si no existe
        adminUserService.createAdminUserIfNotExist();
    }
}