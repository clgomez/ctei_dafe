package com.Tech.Dafe.Email.Controller;


import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Response.Mensaje;
import com.Tech.Dafe.Modules.Autenticacion.Service.AutenticacionServicio;
import com.Tech.Dafe.Email.Dto.ChangePasswordDTO;
import com.Tech.Dafe.Email.Dto.EmailValuesDTO;
import com.Tech.Dafe.Email.Service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email")
@CrossOrigin
public class EmailPasswordController {

    @Autowired
    EmailService emailService;

    @Autowired
    AutenticacionServicio usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private static final String subject = "Cambio de Contraseña";

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/send-email-password")
    public ResponseEntity<?> sendEmailTemplate(@RequestBody EmailValuesDTO dto) {
        Optional<Usuario> userOpt = usuarioService.getBynameOrUsername(dto.getMailTo());
        if(!userOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningún usuario con esas credenciales"), HttpStatus.NOT_FOUND);
        Usuario user = userOpt.get();
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
        return new ResponseEntity(new Mensaje("Te hemos enviado un correo"), HttpStatus.OK);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        if(!dto.getPassword().equals(dto.getConfirmarPassword()))
            return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
        Optional<Usuario> userOpt = usuarioService.getByTokenPassword(dto.getTokenPassword());
        if(!userOpt.isPresent())
            return new ResponseEntity(new Mensaje("No existe ningún usuario con esas credenciales"), HttpStatus.NOT_FOUND);
        Usuario user = userOpt.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(newPassword);
        user.setTokenPassword(null);
        usuarioService.save(user);
        return new ResponseEntity(new Mensaje("Contraseña actualizada"), HttpStatus.OK);
    }

}