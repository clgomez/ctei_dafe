package com.Tech.Dafe.Modules.Enums.Ocupacion.Repository;

import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OcupacionRepository extends JpaRepository<Ocupacion, Long> {
    Optional<Ocupacion> findByNombre(String nombre);
}
