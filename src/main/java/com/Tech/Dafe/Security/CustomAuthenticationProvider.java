package com.Tech.Dafe.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
public Authentication authenticate(Authentication authentication) {

    System.out.println("USANDO CUSTOM PROVIDER");

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    UserDetails userDetails;

    try {
        userDetails = getUserDetailsService().loadUserByUsername(username);
    } catch (UsernameNotFoundException ex) {
        // CLAVE: ocultar si el usuario existe o no
        throw new BadCredentialsException("Credenciales inválidas");
    }

    // 1️⃣ validar contraseña
    if (!getPasswordEncoder().matches(password, userDetails.getPassword())) {
        throw new BadCredentialsException("Credenciales inválidas");
    }

    // 2️⃣ validar estado
    if (!userDetails.isEnabled()) {
        throw new DisabledException("User is disabled");
    }

    return new UsernamePasswordAuthenticationToken(
            userDetails,
            password,
            userDetails.getAuthorities()
    );
}
}