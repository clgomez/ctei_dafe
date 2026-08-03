package com.Tech.Dafe.Modules.Enums.Ocupacion.Utils;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Services.OcupacionesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3)
public class CreateOcupaciones implements CommandLineRunner {

    private final OcupacionesService ocupacionesService;

    @Override
    public void run(String... args) throws Exception {
        createOcupacionIfNotFound("Profesor");
        createOcupacionIfNotFound("Ingeniero");
        createOcupacionIfNotFound("GerenteDeProyecto");
        createOcupacionIfNotFound("CoordinadorDeProyectos");
        createOcupacionIfNotFound("AnalistaDeDatos");
        createOcupacionIfNotFound("Consultor");
        createOcupacionIfNotFound("Estudiante");
        createOcupacionIfNotFound("Aprendiz");

    }

    private void createOcupacionIfNotFound(String nombre) {
        if (!ocupacionesService.existsByNombre(nombre)) {
            Ocupacion ocupacion = new Ocupacion(nombre);
            ocupacionesService.save(ocupacion);
        }
    }
}
