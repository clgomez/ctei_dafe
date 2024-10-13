package com.Tech.Dafe.Modules.Enums.Semaforo.Util;

import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.Tech.Dafe.Modules.Enums.Semaforo.Service.SemaforoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class CreateSemaforo implements CommandLineRunner {

    @Autowired
    SemaforoService semaforoService;

    @Override
    public void run(String... args) throws Exception {
        createSemaforoIfNotFound("Verde");
        createSemaforoIfNotFound("Amarillo");
        createSemaforoIfNotFound("Rojo");
        createSemaforoIfNotFound("Pendiente");
    }

    private void createSemaforoIfNotFound(String nombre) {
        if (!semaforoService.existsByNombre(nombre)) {
            Semaforo semaforo = new Semaforo(nombre);
            semaforoService.save(semaforo);
        }
    }
}
