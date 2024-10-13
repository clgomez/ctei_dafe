package com.Tech.Dafe.Modules.Autenticacion.Service;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacionServicio {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolRepository rolRepository;


    public Optional<Usuario> getBynameOrUsername(String nameOrUsername){
        return userRepository.findByNombreOrUsername(nameOrUsername, nameOrUsername);
    }

    public Optional<Usuario> getByTokenPassword(String tokenPassword){
        return userRepository.findByTokenPassword(tokenPassword);
    }

    public Optional<Usuario> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<Usuario> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByIdentificacion(String identificacion) {
        return userRepository.existsByIdentificacion(identificacion);
    }

    public boolean existsByTelefono(String telefono) {
        return userRepository.existsByTelefono(telefono);
    }


    public void save(Usuario user) {
        userRepository.save(user);
    }

    public Optional<Rol> findRolByNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }


}
