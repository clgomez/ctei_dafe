package com.Tech.Dafe.Modules.Usuario.Controller;


import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Response.Mensaje;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Modules.Usuario.DTO.UsuarioDTO;
import com.Tech.Dafe.Modules.Usuario.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutenticacionServicio autenticacionServicio;



    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok().body(usuarioService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los elementos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> ver(@PathVariable Long id) {
        try {
            Optional<Usuario> o = usuarioService.findById(id);
            if (o.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Elemento no encontrado con ID: " + id);
            }
            return ResponseEntity.ok().body(o.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el elemento: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            if (usuarioDTO == null) {
                return ResponseEntity.badRequest().body("La entidad proporcionada es nula.");
            }

            if (autenticacionServicio.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));
            }
            if (autenticacionServicio.existsByUserName(usuarioDTO.getUsername())) {
                return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));
            }
            if (autenticacionServicio.existsByIdentificacion(usuarioDTO.getIdentificacion())) {
                return ResponseEntity.badRequest().body(new Mensaje("El usuario con esa identificación ya existe"));
            }

            if (autenticacionServicio.existsByTelefono(usuarioDTO.getTelefono()))
                return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya existe"));

            Usuario usuario = new Usuario();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellidos(usuarioDTO.getApellidos());
            usuario.setDireccion(usuarioDTO.getDireccion());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setEstado(usuarioDTO.getEstado());
            usuario.setFecha_nacimiento(usuarioDTO.getFecha_nacimiento());
            usuario.setFecha_registro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            usuario.setTipoIdentificacion(usuarioDTO.getTipoIdentificacion());
            usuario.setIdentificacion(usuarioDTO.getIdentificacion());
            usuario.setGenero(usuarioDTO.getGenero());
            usuario.setOcupacion(usuarioDTO.getOcupacion());
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setTelefono(usuarioDTO.getTelefono());

            for (RolNombre rolNombre : usuarioDTO.getRoles()) {
                Optional<Rol> rolOpt = usuarioService.findRolByNombre(rolNombre);
                if (rolOpt.isPresent()) {
                    usuario.getRoles().add(rolOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("El rol " + rolNombre + " no existe.");
                }
            }

            Usuario usuarioDb = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el elemento: " + e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (!usuarioService.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Elemento no encontrado con ID: " + id);
            }
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el elemento: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> ActualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> o = usuarioService.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (autenticacionServicio.existsByEmail(usuario.getEmail()) && !usuario.getEmail().equals(o.get().getEmail())) {
            return ResponseEntity.badRequest().body(new Mensaje("Ese email ya está en uso"));
        }

        if (autenticacionServicio.existsByIdentificacion(usuario.getIdentificacion()) && !usuario.getIdentificacion().equals(o.get().getIdentificacion())) {
            return ResponseEntity.badRequest().body(new Mensaje("Esa identificación ya está en uso"));
        }

        if (autenticacionServicio.existsByTelefono(usuario.getTelefono()) && !usuario.getTelefono().equals(o.get().getTelefono())) {
            return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya está en uso"));
        }

        Usuario usuarioDb = o.get();
        usuarioDb.setNombre(usuario.getNombre());
        usuarioDb.setApellidos(usuario.getApellidos());
        usuarioDb.setDireccion(usuario.getDireccion());
        usuarioDb.setEmail(usuario.getEmail());
        usuarioDb.setEstado(usuario.getEstado());
        usuarioDb.setFecha_nacimiento(usuario.getFecha_nacimiento());
        usuarioDb.setTipoIdentificacion(usuario.getTipoIdentificacion());
        usuarioDb.setIdentificacion(usuario.getIdentificacion());
        usuarioDb.setGenero(usuario.getGenero());
        usuarioDb.setOcupacion(usuario.getOcupacion());
        usuarioDb.setUsername(usuario.getUsername());
        usuarioDb.setTelefono(usuario.getTelefono());

        return ResponseEntity.ok(usuarioService.save(usuarioDb));
    }


    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Se ha producido un error inesperado: " + e.getMessage());
    }


}
