package com.Tech.Dafe.Modules.Autenticacion.Controladores;

import com.Tech.Dafe.Modules.Autenticacion.DTO.Nuevo_Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Jwt.JwtProvider;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.GlobalExceptionHandler;
import com.Tech.Dafe.Modules.Autenticacion.DTO.LoginUsuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@WebMvcTest(AutenticacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacionServicio authService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistrarUsuario_exitoso() throws Exception {

        Nuevo_Usuario nuevoUsuario = Nuevo_Usuario.builder()
                .nombre("Carlos")
                .apellidos("Gomez")
                .direccion("Calle 123 #45-67")
                .email("carlos@gmail.com")
                .estado("ACTIVO")
                .fechaNacimiento(LocalDate.of(2000, 5, 10))
                .tipoIdentificacion(TipoIdentificacionEnum.CEDULA_CIUDADANIA)
                .identificacion("123456789")
                .username("carlos123")
                .password("Password1!")
                .roles(Set.of("USER"))
                .telefono("3001234567")
                .genero(GeneroEnum.Masculino)
                .ocupacion(OcupacionEnum.Estudiante)
                .build();

        mockMvc.perform(post("/auth/register/investigador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado correctamente"));

        verify(authService).registrarUsuario(nuevoUsuario, RolNombre.ROL_INVESTIGADOR);
    }

    @Test
    void testRegistrarUsuario_datosInvalidos() throws Exception {

        Nuevo_Usuario nuevoUsuario = Nuevo_Usuario.builder()
                .nombre("Carlos")
                .apellidos("Gomez")
                .direccion("Calle 123 #45-67")
                .email("correo-invalido") //Email incorrecto
                .estado("ACTIVO")
                .fechaNacimiento(LocalDate.of(1995, 5, 10))
                .tipoIdentificacion(TipoIdentificacionEnum.CEDULA_CIUDADANIA)
                .identificacion("123456789")
                .username("carlos123")
                .password("Password1!")
                .telefono("3001234567")
                .genero(GeneroEnum.Masculino)
                .ocupacion(OcupacionEnum.Estudiante)
                .build();

        mockMvc.perform(post("/auth/register/investigador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("Formato de email inválido. Ejemplo válido: usuario@dominio.com"));

        // 🔒 Verificar que NO se llamó el servicio
        verify(authService, never())
                .registrarUsuario(any(), any());
    }

    @Test
    void testRegistrarUsuario_emailDuplicado() throws Exception {

        Nuevo_Usuario nuevoUsuario = Nuevo_Usuario.builder()
                .nombre("Carlos")
                .apellidos("Gomez")
                .direccion("Calle 123 #45-67")
                .email("carlos@gmail.com")
                .estado("ACTIVO")
                .fechaNacimiento(LocalDate.of(1995, 5, 10))
                .tipoIdentificacion(TipoIdentificacionEnum.CEDULA_CIUDADANIA)
                .identificacion("123456789")
                .username("carlos123")
                .password("Password1!")
                .telefono("3001234567")
                .genero(GeneroEnum.Masculino)
                .ocupacion(OcupacionEnum.Estudiante)
                .build();

        // Simular que el servicio detecta email duplicado
        doThrow(new BusinessException(List.of(new CampoError("email", "El correo electrónico ya está en uso"))))
        .when(authService)
        .registrarUsuario(any(), eq(RolNombre.ROL_INVESTIGADOR));

        mockMvc.perform(post("/auth/register/investigador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("El correo electrónico ya está en uso"));
    }

    @Test
    void testRegistrarUsuario_camposRequeridosNoCompletos() throws Exception {

        // Usuario con campos obligatorios vacíos
        Nuevo_Usuario nuevoUsuario = Nuevo_Usuario.builder()
                .nombre("") // Campo obligatorio vacío
                .email("")  // Campo obligatorio vacío
                .password("") // Campo obligatorio vacío
                .build();

        mockMvc.perform(post("/auth/register/investigador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        // Verificamos que el servicio NO fue llamado
        verify(authService, never())
                .registrarUsuario(any(), any());
    }

    @Test
    void testLogin_exitoso() throws Exception {

        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setEmail("carlos@gmail.com");
        loginUsuario.setPassword("Password1!");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        loginUsuario.getEmail(),
                        loginUsuario.getPassword()
                );

        // Simular autenticación exitosa
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        // Simular generación de token
        when(jwtProvider.generateToken(authentication))
                .thenReturn("jwt-token-falso-123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUsuario)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("jwt-token-falso-123"));
    }

    @Test
    void testLogin_credencialesInvalidas() throws Exception {

        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setEmail("usuario@gmail.com");
        loginUsuario.setPassword("PasswordIncorrecta");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUsuario)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje")
                        .value("Los datos ingresados no son válidos"));
    }

        @Test
        void testRegistrarUsuario_errorServidor() throws Exception {

        Nuevo_Usuario nuevoUsuario = Nuevo_Usuario.builder()
                .nombre("Carlos")
                .apellidos("Gomez")
                .direccion("Calle 123 #45")
                .email("carlos@gmail.com")
                .estado("ACTIVO")
                .fechaNacimiento(LocalDate.of(1995,5,10))
                .tipoIdentificacion(TipoIdentificacionEnum.CEDULA_CIUDADANIA)
                .identificacion("123456789")
                .username("carlos123")
                .password("Password1!")
                .telefono("3001234567")
                .genero(GeneroEnum.Masculino)
                .ocupacion(OcupacionEnum.Estudiante)
                .build();

        doThrow(new RuntimeException("Error interno"))
                .when(authService)
                .registrarUsuario(any(), any());

        mockMvc.perform(post("/auth/register/investigador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))

                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje")
                .value("Ocurrió un error interno del servidor. Intente nuevamente más tarde o contacte soporte."));
        }
        
        @Test
        void testLogin_errorServidor() throws Exception {

        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setEmail("usuario@gmail.com");
        loginUsuario.setPassword("Password1!");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Error servidor"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUsuario)))

                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje")
                .value("Ocurrió un error interno del servidor. Intente nuevamente más tarde o contacte soporte."));
        }
}


