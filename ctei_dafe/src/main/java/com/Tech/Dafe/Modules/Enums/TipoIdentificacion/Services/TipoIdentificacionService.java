package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Services;

import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;

public interface TipoIdentificacionService {
    void save(TipoIdentificacion tipoIdentificacion);
    boolean existsByNombre(String nombre);
}
