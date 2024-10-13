package com.Tech.Dafe.Modules.Enums.Semaforo.Repository;

import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemaforoRepository extends JpaRepository<Semaforo, Long> {
    Optional<Semaforo> findByNombre(String nombre);

}
