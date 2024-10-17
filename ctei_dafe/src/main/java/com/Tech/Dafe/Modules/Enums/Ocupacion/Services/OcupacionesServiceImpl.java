package com.Tech.Dafe.Modules.Enums.Ocupacion.Services;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Repository.OcupacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OcupacionesServiceImpl implements OcupacionesService {

    @Autowired
    OcupacionRepository ocupacionRepository;

    public void save(Ocupacion ocupacion) {
        ocupacionRepository.save(ocupacion);
    }

    public boolean existsByNombre(String nombre) {
        return ocupacionRepository.findByNombre(nombre).isPresent();
    }

    public boolean existsByOcupacion(String ocupacion) {
        return ocupacionRepository.findByNombre(ocupacion).isPresent();
    }

}
