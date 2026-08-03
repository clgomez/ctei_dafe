package com.Tech.Dafe.Modules.Enums.Genero.Services;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import com.Tech.Dafe.Modules.Enums.Genero.Repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepository;

    public void save(Genero genero) {
        generoRepository.save(genero);
    }

    public boolean existsByNombre(String nombre) {
        return generoRepository.findByNombre(nombre).isPresent();
    }

}
