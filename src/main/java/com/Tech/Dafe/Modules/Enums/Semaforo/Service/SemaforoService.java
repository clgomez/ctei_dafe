package com.Tech.Dafe.Modules.Enums.Semaforo.Service;

import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.Tech.Dafe.Modules.Enums.Semaforo.Repository.SemaforoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemaforoService {

    private final SemaforoRepository semaforoRepository;

    public void save(Semaforo semaforo) {
        semaforoRepository.save(semaforo);
    }

    public boolean existsByNombre(String nombre) {
        return semaforoRepository.findByNombre(nombre).isPresent();
    }

    public boolean existsBySemaforo(String semaforo) {
        return semaforoRepository.findByNombre(semaforo).isPresent();
    }

}


