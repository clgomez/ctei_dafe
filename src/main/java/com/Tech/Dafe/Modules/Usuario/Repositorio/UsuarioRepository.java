package com.Tech.Dafe.Modules.Usuario.Repositorio;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    List<Usuario> findByRoles_RolNombre(RolNombre rolNombre);
    Optional<Usuario> findByIdentificacion(String identificacion);

    // IMPORTANTE: para update (excluyendo el mismo ID)
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByIdentificacionAndIdNot(String identificacion, Long id);
    boolean existsByTelefonoAndIdNot(String telefono, Long id);
    Optional<Usuario> findByEmail(String email);
    
}
