package com.Tech.Dafe.Modules.Autenticacion.Service;

import com.Tech.Dafe.Exceptions.BusinessException;

import com.Tech.Dafe.Modules.Autenticacion.DTO.Nuevo_Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.UserRepository;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Response.CampoError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AutenticacionServicio {

    private final UserRepository userRepository;
    
    private final RolRepository rolRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final RolServicio rolService;

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

    public void registrarUsuario(Nuevo_Usuario dto, RolNombre rolNombre) {

     List<CampoError> errores = new ArrayList<>();    

     if (!"ACTIVO".equalsIgnoreCase(dto.getEstado())) 
        errores.add(new CampoError("estado", "El usuario solo puede registrarse con estado ACTIVO"));
    
    if (existsByUserName(dto.getUsername()))
        errores.add(new CampoError("username", "Ese nombre de usuario ya existe"));
 
    if (existsByEmail(dto.getEmail()))
        errores.add(new CampoError("email", "Ese correo electrónico ya existe"));

    if (existsByIdentificacion(dto.getIdentificacion()))
        errores.add(new CampoError("identificacion", "Esa identificación ya existe"));

    if (existsByTelefono(dto.getTelefono()))
        errores.add(new CampoError("telefono", "Ese teléfono ya existe"));


     // IMPORTANTE: lanzar todos los errores juntos
    if (!errores.isEmpty()) {
        throw new BusinessException(errores);
    }

    Usuario usuario = new Usuario(
            dto.getNombre(),
            dto.getApellidos(),
            dto.getDireccion(),
            dto.getEmail(),
            dto.getEstado(),
            dto.getFechaNacimiento(),
            dto.getTipoIdentificacion(),
            dto.getIdentificacion(),
            dto.getGenero(),
            dto.getOcupacion(),
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getTelefono()
    );

    usuario.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));

    Rol rol = rolService.getByRolName(rolNombre)
        .orElseThrow(() -> new BusinessException(List.of(new CampoError("rolnombre", "El rol "+rolNombre+" no existe en la base de datos"))));

    usuario.setRoles(Set.of(rol));

    userRepository.save(usuario);
}    

    public Optional<Rol> findRolByNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }


}
