package com.Tech.Dafe.Modules.Enums.Genero.Repository;

import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
}
