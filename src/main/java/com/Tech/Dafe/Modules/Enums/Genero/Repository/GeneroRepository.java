package com.Tech.Dafe.Modules.Enums.Genero.Repository;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
}
