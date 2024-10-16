package com.Tech.Dafe.Modules.Enums.Genero.Services;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import com.Tech.Dafe.Modules.Enums.Genero.Repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneroServiceImpl implements GeneroService {

    @Autowired
    GeneroRepository generoRepository;

    public void save(Genero genero) {
        generoRepository.save(genero);
    }

    public boolean existsByNombre(String nombre) {
        return generoRepository.findByNombre(nombre).isPresent();
    }

}
