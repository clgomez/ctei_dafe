package com.Tech.Dafe.Modules.Autenticacion.Controladores;

import com.Tech.Dafe.Modules.Autenticacion.DTO.JwtDto;
import com.Tech.Dafe.Modules.Autenticacion.DTO.LoginUsuario;
import com.Tech.Dafe.Modules.Autenticacion.DTO.Nuevo_Usuario;
import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Autenticacion.Jwt.JwtProvider;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Services.OcupacionesService;
import com.Tech.Dafe.Response.Mensaje;
import com.Tech.Dafe.Modules.Autenticacion.Service.RolServicio;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Modules.Enums.Genero.Services.GeneroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
    @RequestMapping("/auth")
    @CrossOrigin
    public class AutenticacionController {

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        AutenticacionServicio userService;

        @Autowired
        GeneroService generoService;

        @Autowired
        RolServicio rolService;

        @Autowired
        JwtProvider jwtProvider;

        @Autowired
        OcupacionesService OcupacionesService;


        //Registrar un Investigador
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @PostMapping("/investigador")
        public ResponseEntity<?> Investigador(@Valid @RequestBody Nuevo_Usuario nuevoUsuario, BindingResult bindingResult) {

            if (nuevoUsuario.getNombre() == null || nuevoUsuario.getNombre().isEmpty() ||
                    nuevoUsuario.getApellidos() == null || nuevoUsuario.getApellidos().isEmpty() ||
                    nuevoUsuario.getUsername() == null || nuevoUsuario.getUsername().isEmpty() ||
                    nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().isEmpty() ||
                    nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isEmpty() ||
                    nuevoUsuario.getDireccion() == null || nuevoUsuario.getDireccion().isEmpty() ||
                    nuevoUsuario.getIdentificacion() == null || nuevoUsuario.getIdentificacion().isEmpty() ||
                    nuevoUsuario.getTelefono() == null || nuevoUsuario.getTelefono().isEmpty() ||
                    nuevoUsuario.getFecha_nacimiento() == null || nuevoUsuario.getFecha_nacimiento().isEmpty() ||
                    nuevoUsuario.getEstado() == null || nuevoUsuario.getEstado().isEmpty()) {

                return ResponseEntity.badRequest().body(new Mensaje("Por favor, rellene todos los campos son obligatorios"));
            }

            if (bindingResult.hasErrors())
                return ResponseEntity.badRequest().body(new Mensaje("Campos mal puestos o Email inválido"));

            if (userService.existsByUserName(nuevoUsuario.getUsername()))
                return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));

            if (userService.existsByEmail(nuevoUsuario.getEmail()))
                return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));

            if (userService.existsByIdentificacion(nuevoUsuario.getIdentificacion()))
                return ResponseEntity.badRequest().body(new Mensaje("El usuario con esa identificación ya existe"));

            if (userService.existsByTelefono(nuevoUsuario.getTelefono()))
                return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya existe"));

            Usuario usuario = new Usuario(
                    nuevoUsuario.getNombre(),
                    nuevoUsuario.getApellidos(),
                    nuevoUsuario.getDireccion(),
                    nuevoUsuario.getEmail(),
                    nuevoUsuario.getEstado(),
                    nuevoUsuario.getFecha_nacimiento(),
                    nuevoUsuario.getTipoIdentificacion(),
                    nuevoUsuario.getIdentificacion(),
                    nuevoUsuario.getGenero(),
                    nuevoUsuario.getOcupacion(),
                    nuevoUsuario.getUsername(),
                    passwordEncoder.encode(nuevoUsuario.getPassword()),
                    nuevoUsuario.getTelefono()
            );

            usuario.setFecha_registro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            Set<Rol> roles = new HashSet<>();
            roles.add(rolService.getByRolName(RolNombre.ROL_INVESTIGADOR).get());

            if (nuevoUsuario.getRoles().contains("admin"))
                roles.add(rolService.getByRolName(RolNombre.ROL_INVESTIGADOR).get());

            usuario.setRoles(roles);
            userService.save(usuario);

            return new ResponseEntity(new Mensaje("Investigador guardado satisfactoriamente"), HttpStatus.CREATED);
        }


    //Registrar un Tutor
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/tutor")
    public ResponseEntity<?> Tutor(@Valid @RequestBody Nuevo_Usuario nuevoUsuario, BindingResult bindingResult) {

        if (nuevoUsuario.getNombre() == null || nuevoUsuario.getNombre().isEmpty() ||
                nuevoUsuario.getApellidos() == null || nuevoUsuario.getApellidos().isEmpty() ||
                nuevoUsuario.getUsername() == null || nuevoUsuario.getUsername().isEmpty() ||
                nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().isEmpty() ||
                nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isEmpty() ||
                nuevoUsuario.getDireccion() == null || nuevoUsuario.getDireccion().isEmpty() ||
                nuevoUsuario.getIdentificacion() == null || nuevoUsuario.getIdentificacion().isEmpty() ||
                nuevoUsuario.getTelefono() == null || nuevoUsuario.getTelefono().isEmpty() ||
                nuevoUsuario.getFecha_nacimiento() == null || nuevoUsuario.getFecha_nacimiento().isEmpty() ||
                nuevoUsuario.getEstado() == null || nuevoUsuario.getEstado().isEmpty()) {

            return ResponseEntity.badRequest().body(new Mensaje("Por favor, rellene todos los campos son obligatorios"));
        }

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(new Mensaje("Campos mal puestos o Email inválido"));

        if (userService.existsByUserName(nuevoUsuario.getUsername()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));

        if (userService.existsByEmail(nuevoUsuario.getEmail()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));

        if (userService.existsByIdentificacion(nuevoUsuario.getIdentificacion()))
            return ResponseEntity.badRequest().body(new Mensaje("El usuario con esa identificación ya existe"));

        if (userService.existsByTelefono(nuevoUsuario.getTelefono()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya existe"));

        Usuario usuario = new Usuario(
                nuevoUsuario.getNombre(),
                nuevoUsuario.getApellidos(),
                nuevoUsuario.getDireccion(),
                nuevoUsuario.getEmail(),
                nuevoUsuario.getEstado(),
                nuevoUsuario.getFecha_nacimiento(),
                nuevoUsuario.getTipoIdentificacion(),
                nuevoUsuario.getIdentificacion(),
                nuevoUsuario.getGenero(),
                nuevoUsuario.getOcupacion(),
                nuevoUsuario.getUsername(),
                passwordEncoder.encode(nuevoUsuario.getPassword()),
                nuevoUsuario.getTelefono()
        );

        usuario.setFecha_registro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolNombre.ROL_TUTOR).get());

        if (nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolNombre.ROL_TUTOR).get());

        usuario.setRoles(roles);
        userService.save(usuario);

        return new ResponseEntity(new Mensaje("Tutor guardado satisfactoriamente"), HttpStatus.CREATED);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/evaluador")
    public ResponseEntity<?> Evaluador(@Valid @RequestBody Nuevo_Usuario nuevoUsuario, BindingResult bindingResult) {

        if (nuevoUsuario.getNombre() == null || nuevoUsuario.getNombre().isEmpty() ||
                nuevoUsuario.getApellidos() == null || nuevoUsuario.getApellidos().isEmpty() ||
                nuevoUsuario.getUsername() == null || nuevoUsuario.getUsername().isEmpty() ||
                nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().isEmpty() ||
                nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isEmpty() ||
                nuevoUsuario.getDireccion() == null || nuevoUsuario.getDireccion().isEmpty() ||
                nuevoUsuario.getIdentificacion() == null || nuevoUsuario.getIdentificacion().isEmpty() ||
                nuevoUsuario.getTelefono() == null || nuevoUsuario.getTelefono().isEmpty() ||
                nuevoUsuario.getFecha_nacimiento() == null || nuevoUsuario.getFecha_nacimiento().isEmpty() ||
                nuevoUsuario.getEstado() == null || nuevoUsuario.getEstado().isEmpty()) {

            return ResponseEntity.badRequest().body(new Mensaje("Por favor, rellene todos los campos son obligatorios"));
        }

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(new Mensaje("Campos mal puestos o Email inválido"));

        if (userService.existsByUserName(nuevoUsuario.getUsername()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));

        if (userService.existsByEmail(nuevoUsuario.getEmail()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));

        if (userService.existsByIdentificacion(nuevoUsuario.getIdentificacion()))
            return ResponseEntity.badRequest().body(new Mensaje("El usuario con esa identificación ya existe"));

        if (userService.existsByTelefono(nuevoUsuario.getTelefono()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya existe"));

        Usuario usuario = new Usuario(
                nuevoUsuario.getNombre(),
                nuevoUsuario.getApellidos(),
                nuevoUsuario.getDireccion(),
                nuevoUsuario.getEmail(),
                nuevoUsuario.getEstado(),
                nuevoUsuario.getFecha_nacimiento(),
                nuevoUsuario.getTipoIdentificacion(),
                nuevoUsuario.getIdentificacion(),
                nuevoUsuario.getGenero(),
                nuevoUsuario.getOcupacion(),
                nuevoUsuario.getUsername(),
                passwordEncoder.encode(nuevoUsuario.getPassword()),
                nuevoUsuario.getTelefono()
        );

        usuario.setFecha_registro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolNombre.ROL_EVALUADOR).get());

        if (nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolNombre.ROL_EVALUADOR).get());

        usuario.setRoles(roles);
        userService.save(usuario);

        return new ResponseEntity(new Mensaje("Evaluador guardado satisfactoriamente"), HttpStatus.CREATED);
    }

    //Registrar un Administrador
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/admin")
    public ResponseEntity<?> Administrador(@Valid @RequestBody Nuevo_Usuario nuevoUsuario, BindingResult bindingResult) {

        if (nuevoUsuario.getNombre() == null || nuevoUsuario.getNombre().isEmpty() ||
                nuevoUsuario.getApellidos() == null || nuevoUsuario.getApellidos().isEmpty() ||
                nuevoUsuario.getUsername() == null || nuevoUsuario.getUsername().isEmpty() ||
                nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().isEmpty() ||
                nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().isEmpty() ||
                nuevoUsuario.getDireccion() == null || nuevoUsuario.getDireccion().isEmpty() ||
                nuevoUsuario.getIdentificacion() == null || nuevoUsuario.getIdentificacion().isEmpty() ||
                nuevoUsuario.getTelefono() == null || nuevoUsuario.getTelefono().isEmpty() ||
                nuevoUsuario.getFecha_nacimiento() == null || nuevoUsuario.getFecha_nacimiento().isEmpty() ||
                nuevoUsuario.getEstado() == null || nuevoUsuario.getEstado().isEmpty()) {

            return ResponseEntity.badRequest().body(new Mensaje("Por favor, rellene todos los campos son obligatorios"));
        }

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(new Mensaje("Campos mal puestos o Email inválido"));

        if (userService.existsByUserName(nuevoUsuario.getUsername()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));

        if (userService.existsByEmail(nuevoUsuario.getEmail()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));

        if (userService.existsByIdentificacion(nuevoUsuario.getIdentificacion()))
            return ResponseEntity.badRequest().body(new Mensaje("El usuario con esa identificación ya existe"));

        if (userService.existsByTelefono(nuevoUsuario.getTelefono()))
            return ResponseEntity.badRequest().body(new Mensaje("Ese teléfono ya existe"));

        Usuario usuario = new Usuario(
                nuevoUsuario.getNombre(),
                nuevoUsuario.getApellidos(),
                nuevoUsuario.getDireccion(),
                nuevoUsuario.getEmail(),
                nuevoUsuario.getEstado(),
                nuevoUsuario.getFecha_nacimiento(),
                nuevoUsuario.getTipoIdentificacion(),
                nuevoUsuario.getIdentificacion(),
                nuevoUsuario.getGenero(),
                nuevoUsuario.getOcupacion(),
                nuevoUsuario.getUsername(),
                passwordEncoder.encode(nuevoUsuario.getPassword()),
                nuevoUsuario.getTelefono()
        );

        usuario.setFecha_registro(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolNombre.ROL_ADMINISTRADOR).get());

        if (nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolNombre.ROL_ADMINISTRADOR).get());

        usuario.setRoles(roles);
        userService.save(usuario);

        return new ResponseEntity(new Mensaje("Administrador guardado satisfactoriamente"), HttpStatus.CREATED);
    }


        //Authentication del usuario
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new Mensaje("Por favor, rellene todos los campos"), HttpStatus.BAD_REQUEST);

            if (!userService.existsByEmail(loginUsuario.getEmail())) {
                return new ResponseEntity<>(new Mensaje("Usuario no encontrado"), HttpStatus.UNAUTHORIZED);
            }

            try {
                Authentication authentication =
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(), loginUsuario.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtProvider.generateToken(authentication);

                JwtDto jwtDto = new JwtDto(jwt);

                return new ResponseEntity(jwtDto, HttpStatus.OK);
            } catch (org.springframework.security.authentication.BadCredentialsException e) {
                return new ResponseEntity<>(new Mensaje("Contraseña incorrecta"), HttpStatus.UNAUTHORIZED);
            }
        }


        @PostMapping("/refresh")
        public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
            String token = jwtProvider.refreshToken(jwtDto);
            JwtDto jwt = new JwtDto(token);
            return  new ResponseEntity<>(jwt, HttpStatus.OK);
        }

        @GetMapping("/email/{email}")
        public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@PathVariable String email) {
        Optional<Usuario> optUsuario = userService.getByEmail(email);
        Usuario usuario = optUsuario.get();
        return usuario != null ? new ResponseEntity<>(usuario, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    }
