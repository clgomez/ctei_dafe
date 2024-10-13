package com.Tech.Dafe.Modules.Usuario.Services;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;

import java.util.Optional;

public interface UsuarioService {

    public Iterable<Usuario> findAll();
    public Optional<Usuario> findById(Long id);
    public Usuario save(Usuario usuario);
    public void deleteById(Long id);
    Optional<Rol> findRolByNombre(RolNombre rolNombre);

}
