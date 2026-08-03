package com.Tech.Dafe.Email.Controller;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Response.Mensaje;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Email.Dto.ChangePasswordDTO;
import com.Tech.Dafe.Email.Dto.EmailValuesDTO;
import com.Tech.Dafe.Email.Service.EmailService;
import com.Tech.Dafe.Exceptions.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email")
@CrossOrigin
@RequiredArgsConstructor
public class EmailPasswordController {

    
    private final EmailService emailService;
   
    private final AutenticacionServicio usuarioService;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private static final String subject = "Cambio de Contraseña";

    @PostMapping("/send-email-password")
    public ResponseEntity<?> sendEmailTemplate(@Valid @RequestBody EmailValuesDTO dto) {
        
        Optional<Usuario> userOpt = usuarioService.getBynameOrUsername(dto.getMailTo());

        if(userOpt.isEmpty()) {
            throw new BusinessException
            (List.of(new CampoError("mailTo", "El nombre de usuario no está registrado. Verifique el dato ingresado o regístrese.")));
        }

        Usuario user = userOpt.get();

        if(!"ACTIVO".equals(user.getEstado())) {
            throw new BusinessException(List.of(new CampoError("estado","La cuenta está desactivada. Contacte con soporte técnico")));
        }

        dto.setMailFrom(mailFrom);
        dto.setMailTo(user.getEmail());
        dto.setSubject(subject);
        dto.setUsername(user.getUsername());

        UUID uuid = UUID.randomUUID();
        String tokenPassword = uuid.toString();

        dto.setTokenPassword(tokenPassword);
        user.setTokenPassword(tokenPassword);

        usuarioService.save(user);

        emailService.sendEmail(dto);

        return ResponseEntity.ok(new Mensaje("Te hemos enviado un correo"));
            
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {

        if(!dto.getPassword().equals(dto.getConfirmarPassword())) {
            throw new BusinessException(
                List.of(new CampoError("password", "Las contraseñas no coinciden"))
            );
        }

        Optional<Usuario> userOpt = usuarioService.getByTokenPassword(dto.getTokenPassword());

        if(userOpt.isEmpty()) {
            throw new BusinessException(
                List.of(new CampoError("tokenPassword", 
                    "El token es inválido o ha expirado. Solicita un nuevo enlace."))
            );
        }

        Usuario user = userOpt.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(newPassword);
        user.setTokenPassword(null);
        usuarioService.save(user);
        
        return ResponseEntity.ok(
            Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Cambio exitoso, puedes iniciar sesión"
            )
        );
    }
    
}