package com.Tech.Dafe.Modules.Enums.Genero.Services;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;

public interface GeneroService {
    void save(Genero genero);
    boolean existsByNombre(String nombre);
}
