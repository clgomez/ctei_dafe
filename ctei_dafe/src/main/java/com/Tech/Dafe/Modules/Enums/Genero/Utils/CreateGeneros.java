package com.Tech.Dafe.Modules.Enums.Genero.Utils;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import com.Tech.Dafe.Modules.Enums.Genero.Services.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CreateGeneros implements CommandLineRunner {

    @Autowired
    GeneroService generoService;

    @Override
    public void run(String... args) throws Exception {
        createGeneroIfNotFound("Masculino");
        createGeneroIfNotFound("Femenino");
        createGeneroIfNotFound("Otro");
    }

    private void createGeneroIfNotFound(String nombre) {
        if (!generoService.existsByNombre(nombre)) {
            Genero genero = new Genero(nombre);
            generoService.save(genero);
        }
    }
}
