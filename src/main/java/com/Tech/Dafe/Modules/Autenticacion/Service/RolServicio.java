package com.Tech.Dafe.Modules.Autenticacion.Service;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolServicio {
    @Autowired
    private RolRepository rolRepository;

    public Optional<Rol> getByRolName(RolNombre rolName) {
        return rolRepository.findByRolNombre(rolName);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}