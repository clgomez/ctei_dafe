package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Utils;


import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Services.TipoIdentificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CreateTipoIdentificacion implements CommandLineRunner {

    @Autowired
    TipoIdentificacionService tipoIdentificacionService;

    @Override
    public void run(String... args) throws Exception {
        createTipoIDentificacionIfNotFound("CEDULA_CIUDADANIA");
        createTipoIDentificacionIfNotFound("CEDULA_EXTRANJERIA");
        createTipoIDentificacionIfNotFound("PASAPORTE");
        createTipoIDentificacionIfNotFound("TARJETA_DE_IDENTIDAD");
        createTipoIDentificacionIfNotFound("NUMERO_DE_IDENTIFICACION_FISCAL");
        createTipoIDentificacionIfNotFound("LICENCIA_DE_CONDUCIR");
        createTipoIDentificacionIfNotFound("REGISTRO_CIVIL");
        createTipoIDentificacionIfNotFound("IDENTIFICACION_MILITAR");
    }

    private void createTipoIDentificacionIfNotFound(String nombre) {
        if (!tipoIdentificacionService.existsByNombre(nombre)) {
            TipoIdentificacion tipoIdentificacion = new TipoIdentificacion(nombre);
            tipoIdentificacionService.save(tipoIdentificacion);
        }
    }
}
