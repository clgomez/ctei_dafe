package com.Tech.Dafe.Modules.Enums.Roles.Service;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import com.Tech.Dafe.Modules.Enums.Roles.DTO.RolDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    
    private final RolRepository rolRepository;

    public RolDTO crearRol(RolDTO rolDTO) {
       
        Rol rol = new Rol();
        rol.setRolNombre(rolDTO.getRolNombre());
 
        rol = rolRepository.save(rol);
  
        return mapToDTO(rol);
    }


    @Override
    public Optional<RolDTO> obtenerRol(Long id) {
        return rolRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public RolDTO actualizarRol(Long id, RolDTO rolActualizado) {
        return rolRepository.findById(id).map(rol -> {
            rol.setRolNombre(rolActualizado.getRolNombre());
           
            return mapToDTO(rolRepository.save(rol));
        }).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
    }


    @Override
    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public List<RolDTO> listarRoles() {
        return rolRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RolDTO mapToDTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setRolNombre(rol.getRolNombre());
        
        return dto;
    }


}
