package com.Tech.Dafe.Modules.Autenticacion.Repositorio;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreOrUsername(String nombre, String username);
    Optional<Usuario> findByTokenPassword(String tokenPassword);
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByIdentificacion(String identificacion);
    boolean existsByTelefono(String telefono);
    Optional<Usuario> findByEmail(String email);

}
