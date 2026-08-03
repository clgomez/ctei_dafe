package com.Tech.Dafe.Modules.Autenticacion.Controladores;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Autenticacion.DTO.JwtDto;
import com.Tech.Dafe.Modules.Autenticacion.DTO.LoginUsuario;
import com.Tech.Dafe.Modules.Autenticacion.DTO.Nuevo_Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Jwt.JwtProvider;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Response.SuccessResponse;

import jakarta.validation.Valid;

import java.text.ParseException;
import java.util.List;
import java.util.Arrays;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AutenticacionController {

    private final AutenticacionServicio authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AutenticacionController(
            AutenticacionServicio authService,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    // ==========================
    // REGISTRO GENERAL
    // ==========================
    @PostMapping("/register/{rol}")
    public ResponseEntity<?> registrarUsuario(
            @PathVariable String rol,
            @Valid @RequestBody Nuevo_Usuario nuevoUsuario) {
    
    // VALIDAR ROL PRIMERO
        RolNombre rolNombre = Arrays.stream(RolNombre.values())
        .filter(r -> r.name().equals("ROL_" + rol.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new BusinessException(List.of(new CampoError("rolnombre", "El rol ingresado no es válido")
    )));
  
        authService.registrarUsuario(nuevoUsuario, rolNombre);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(
                            "SUCCESS",
                            "Registro exitoso, inicia sesión"
                    ));
     }

    // ==========================
    // LOGIN
    // ==========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario) {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginUsuario.getEmail(),
                                    loginUsuario.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);

            return ResponseEntity.ok(new JwtDto(jwt));

    }

    // ==========================
    // REFRESH TOKEN
    // ==========================
   @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {

        if (jwtDto.getToken() == null || jwtDto.getToken().isBlank()) {
            throw new BusinessException(
                List.of(new CampoError("token", "El token es obligatorio"))
            );
        }

        String token = jwtProvider.refreshToken(jwtDto);

        if (token == null) {
            throw new BusinessException(
                List.of(new CampoError("token", "Token inválido o expirado"))
            );
        }

        return ResponseEntity.ok(new JwtDto(token));
    }
   
     
    /* @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        return ResponseEntity.ok(new JwtDto(token));
    }
    */



    // ==========================
    // OBTENER USUARIO POR EMAIL
    // ==========================
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerUsuarioPorEmail(@PathVariable String email) {

        Usuario usuario = authService.getByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con email: " + email));

        return ResponseEntity.ok(usuario);

        
    }
    
}

