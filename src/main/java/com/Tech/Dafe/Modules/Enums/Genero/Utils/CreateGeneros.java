package com.Tech.Dafe.Modules.Enums.Genero.Utils;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import com.Tech.Dafe.Modules.Enums.Genero.Services.GeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
public class CreateGeneros implements CommandLineRunner {

     private final GeneroService generoService;

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
