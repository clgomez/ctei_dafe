package com.Tech.Dafe.Modules.Autenticacion.Service;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServicio {
    
    private final RolRepository rolRepository;

    public Optional<Rol> getByRolName(RolNombre rolName) {
        return rolRepository.findByRolNombre(rolName);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}