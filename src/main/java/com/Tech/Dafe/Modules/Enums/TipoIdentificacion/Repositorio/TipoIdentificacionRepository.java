package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Repositorio;

import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Long> {
    Optional<TipoIdentificacion> findByNombre(String nombre);
}
