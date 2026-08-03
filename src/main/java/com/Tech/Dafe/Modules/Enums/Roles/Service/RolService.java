package com.Tech.Dafe.Modules.Enums.Roles.Service;

import java.util.List;
import java.util.Optional;

import com.Tech.Dafe.Modules.Enums.Roles.DTO.RolDTO;

public interface RolService {

    RolDTO crearRol(RolDTO rol);
    Optional<RolDTO> obtenerRol(Long id);
    RolDTO actualizarRol(Long id, RolDTO rolActualizado);
    void eliminarRol(Long id);
    List<RolDTO> listarRoles();
    

    
}






