package com.Tech.Dafe.Modules.Enums.Ocupacion.Repository;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OcupacionRepository extends JpaRepository<Ocupacion, Long> {
    Optional<Ocupacion> findByNombre(String nombre);
}
