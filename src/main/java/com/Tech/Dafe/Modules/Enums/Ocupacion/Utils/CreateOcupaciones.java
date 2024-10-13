package com.Tech.Dafe.Modules.Enums.Ocupacion.Utils;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Services.OcupacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class CreateOcupaciones implements CommandLineRunner {

    @Autowired
    OcupacionesService ocupacionesService;

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
