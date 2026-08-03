package com.Tech.Dafe.Email.Controller;

import com.Tech.Dafe.Email.Dto.ChangePasswordDTO;
import com.Tech.Dafe.Email.Dto.EmailValuesDTO;
import com.Tech.Dafe.Email.Service.EmailService;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmailPasswordController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private AutenticacionServicio usuarioService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testSendEmailPassword_exitoso() throws Exception {

        EmailValuesDTO dto = new EmailValuesDTO();
        dto.setMailTo("carlos123");

        Usuario usuario = new Usuario();
        usuario.setUsername("carlos123");
        usuario.setEmail("carlos@gmail.com");
        usuario.setEstado("ACTIVO");

        when(usuarioService.getBynameOrUsername("carlos123"))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/email/send-email-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje")
                        .value("Te hemos enviado un correo"));

        verify(usuarioService).save(any(Usuario.class));
        verify(emailService).sendEmail(any(EmailValuesDTO.class));
    }

    @Test
    void testChangePassword_exitoso() throws Exception {

        // DTO que simula lo que envía el usuario
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setTokenPassword("token-123");
        dto.setPassword("Password1!");
        dto.setConfirmarPassword("Password1!");

        // Usuario encontrado por token
        Usuario usuario = new Usuario();
        usuario.setUsername("carlos123");
        usuario.setEmail("carlos@gmail.com");
        usuario.setEstado("ACTIVO");
        usuario.setTokenPassword("token-123");

        // Simular búsqueda por token
        when(usuarioService.getByTokenPassword("token-123"))
                .thenReturn(Optional.of(usuario));

        // Simular encriptación
        when(passwordEncoder.encode("Password1!"))
                .thenReturn("password-encriptado");

        mockMvc.perform(post("/email/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje")
                        .value("Contraseña actualizada"));

        verify(passwordEncoder).encode("Password1!");
        verify(usuarioService).save(any(Usuario.class));
    }


  @Test
    void testChangePassword_tokenInvalido() throws Exception {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setTokenPassword("token-invalido");
        dto.setPassword("Password123!");
        dto.setConfirmarPassword("Password123!");

        // Simular que no existe usuario con ese token
        when(usuarioService.getByTokenPassword("token-invalido"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/email/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                .value("El token es inválido o ha expirado. Solicita un nuevo enlace."));

        // Verificar que NO se guarda nada
        verify(usuarioService, never()).save(any());
    }

    @Test
    void testSendEmailPassword_cuentaDesactivada() throws Exception {

        EmailValuesDTO dto = new EmailValuesDTO();
        dto.setMailTo("usuario@gmail.com");

        // Simular usuario existente pero desactivado
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUsername("usuario123");
        usuario.setEstado("NO_ACTIVO");

        when(usuarioService.getBynameOrUsername("usuario@gmail.com"))
        .thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/email/send-email-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))

        .andDo(print())

        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.mensaje")
        .value("La cuenta está desactivada. Contacte con soporte técnico."));

        // Verificar que NO se envía correo
        verify(emailService, never()).sendEmail(any());

        // Verificar que NO se guarda usuario
        verify(usuarioService, never()).save(any());
    }

    @Test
    void testSendEmailPassword_usuarioNoRegistrado() throws Exception {

        EmailValuesDTO dto = new EmailValuesDTO();
        dto.setMailTo("usuario_no_registrado@gmail.com");

        // Simular que el usuario NO existe
        when(usuarioService.getBynameOrUsername("usuario_no_registrado@gmail.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/email/send-email-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                .andDo(print())

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje")
                .value("El nombre de usuario o correo no está registrado. Verifique el dato ingresado o regístrese."));

        // Verificar que NO se envía correo
        verify(emailService, never()).sendEmail(any());

        // Verificar que NO se guarda usuario
        verify(usuarioService, never()).save(any());
     }

       @Test
        void testSendEmailPassword_errorServidor() throws Exception {

        EmailValuesDTO dto = new EmailValuesDTO();
        dto.setMailTo("usuario@gmail.com");

        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUsername("usuario");
        usuario.setEstado("ACTIVO");

        when(usuarioService.getBynameOrUsername(any()))
                .thenReturn(Optional.of(usuario));

        // Simular error al enviar correo
        doThrow(new RuntimeException("Error servidor"))
                .when(emailService)
                .sendEmail(any());

        mockMvc.perform(post("/email/send-email-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                .andDo(print())

                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje")
                        .value("Ocurrió un error interno del servidor. Intente nuevamente más tarde o contacte soporte."));

        // Verificar que sí se intentó enviar el correo
        verify(emailService).sendEmail(any());

        // Verificar que se intentó guardar el usuario
        verify(usuarioService).save(any());
        }

	@Test
        void testChangePassword_errorServidor() throws Exception {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setTokenPassword("token-123");
        dto.setPassword("Password123!");
        dto.setConfirmarPassword("Password123!");

        Usuario usuario = new Usuario();
        usuario.setUsername("carlos123");
        usuario.setEmail("carlos@gmail.com");
        usuario.setEstado("ACTIVO");
        usuario.setTokenPassword("token-123");

        // Simular que el usuario existe
        when(usuarioService.getByTokenPassword("token-123"))
                .thenReturn(Optional.of(usuario));

        // Simular encriptación
        when(passwordEncoder.encode("Password123!"))
                .thenReturn("password-encriptado");

        // Simular error del servidor al guardar
        doThrow(new RuntimeException("Error interno"))
                .when(usuarioService)
                .save(any(Usuario.class));

        mockMvc.perform(post("/email/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                .andDo(print())
                .andExpect(status().isInternalServerError());

        // Verificar que sí intentó guardar
        verify(usuarioService).save(any(Usuario.class));
        }

}