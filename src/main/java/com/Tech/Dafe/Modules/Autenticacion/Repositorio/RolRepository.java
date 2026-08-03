package com.Tech.Dafe.Modules.Autenticacion.Repositorio;


import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}