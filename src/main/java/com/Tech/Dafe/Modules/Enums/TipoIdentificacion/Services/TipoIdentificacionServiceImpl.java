package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Services;

import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Repositorio.TipoIdentificacionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoIdentificacionServiceImpl implements TipoIdentificacionService {

    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    public void save(TipoIdentificacion tipoIdentificacion) {
        tipoIdentificacionRepository.save(tipoIdentificacion);
    }

    public boolean existsByNombre(String nombre) {
        return tipoIdentificacionRepository.findByNombre(nombre).isPresent();
    }

}
