package com.Tech.Dafe.Modules.Usuario.Controller;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Usuario.DTO.UsuarioUpdateDTO;
import com.Tech.Dafe.Modules.Usuario.Services.UsuarioService;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
   
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> ver(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
         return ResponseEntity.ok(
            new SuccessResponse(
                    "SUCCESS",
                    "Usuario eliminado con éxito"
            ));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioUpdateDTO);
             return ResponseEntity.ok(
            Map.of(
                    "codigo", "SUCCESS",
                    "mensaje", "Usuario actualizado correctamente",
                    "usuario", usuarioActualizado
                  )
            );
    
    }
     
    @GetMapping("/rol/{nombreRol}")
    public ResponseEntity<?> obtenerUsuarioPorRol(@PathVariable String nombreRol) {

        RolNombre rolNombreEnum;

        try {
            rolNombreEnum = RolNombre.valueOf(nombreRol);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(List.of(
                new CampoError("rol", "El rol proporcionado no es válido: " + nombreRol)
            ));
        }

        List<Usuario> usuarios = usuarioService.findByRoles_RolNombre(rolNombreEnum);

        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron usuarios con el rol: " + nombreRol
            );
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/identificacion/{identificacionUsuario}")
    public ResponseEntity<?> obtenerUsuarioPorIdentificacion(
            @PathVariable String identificacionUsuario) {

        Usuario usuario = usuarioService.obtenerUsuarioPorIdentificacion(identificacionUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con identificación: " + identificacionUsuario));

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/emailusuario/{email}")
    public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@PathVariable String email) {

        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con email: " + email));

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/proyecto/{proyectoId}/usuarioinvestigador/{usuarioInvestigadorId}/usuarioevaluador/{usuarioEvaluadorId}")
    public ResponseEntity<Usuario> obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioInvestigadorId,
            @PathVariable Long usuarioEvaluadorId) {

        Usuario usuarioTutor =
                usuarioService.obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(
                        proyectoId,
                        usuarioInvestigadorId,
                        usuarioEvaluadorId);

        return ResponseEntity.ok(usuarioTutor);
    }


    @GetMapping("/proyecto/{proyectoId}/usuarioinvestigador/{usuarioInvestigadorId}/usuariotutor/{usuarioTutorId}")
    public ResponseEntity<Usuario> obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioInvestigadorId,
            @PathVariable Long usuarioTutorId) {

        Usuario usuarioEvaluador =
                usuarioService.obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(
                        proyectoId,
                        usuarioInvestigadorId,
                        usuarioTutorId);

        return ResponseEntity.ok(usuarioEvaluador);
    }    

}
