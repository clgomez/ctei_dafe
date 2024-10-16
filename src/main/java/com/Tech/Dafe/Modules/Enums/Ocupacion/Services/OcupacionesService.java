package com.Tech.Dafe.Modules.Enums.Ocupacion.Services;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;

public interface OcupacionesService {

    void save(Ocupacion ocupacion);
    boolean existsByNombre(String nombre);

    boolean existsByOcupacion(String ocupacion);
}
