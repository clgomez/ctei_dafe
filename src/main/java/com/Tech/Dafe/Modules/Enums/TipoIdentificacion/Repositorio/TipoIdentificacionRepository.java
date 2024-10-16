package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Repositorio;

import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Long> {
    Optional<TipoIdentificacion> findByNombre(String nombre);
}
