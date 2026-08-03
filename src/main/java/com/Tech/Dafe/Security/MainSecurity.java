package com.Tech.Dafe.Security;

import com.Tech.Dafe.Modules.Autenticacion.Jwt.JwtEntryPoint;
import com.Tech.Dafe.Modules.Autenticacion.Jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MainSecurity {

    /*@Autowired
    UserDetailsServiceImpl userDetailsService;
    */
    
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;

    /*@Bean
    JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }*/

    /*@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    /*@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }*/

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationProvider provider) throws Exception {
        http
                .authenticationProvider(provider) //AQUÍ ESTÁ LA CLAVE
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exc -> exc.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/email/**").permitAll()
                                .requestMatchers("/convocatorias/**").permitAll()
                                .requestMatchers("/proyecto/**").permitAll()
                                .requestMatchers("/notificaciones/**").permitAll()
                                .requestMatchers("/inscripciones/**").permitAll()
                                .requestMatchers("/usuario/**").permitAll()
                                .requestMatchers("/api/asignacion-roles/**").permitAll()
                                .requestMatchers("/arboles-problemas/**").permitAll()
                                .requestMatchers("/causa/**").permitAll()
                                .requestMatchers("/efecto/**").permitAll()
                                .requestMatchers("/arboles-objetivos/**").permitAll()
                                .requestMatchers("/medios/**").permitAll()
                                .requestMatchers("/fines/**").permitAll()
                                .requestMatchers("/actividades/**").permitAll()
                                .requestMatchers("/cronogramas/**").permitAll()
                                .requestMatchers("/roles/**").permitAll()
                                .requestMatchers("/calificaciones/**").permitAll()
                                .requestMatchers("/api/files/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}