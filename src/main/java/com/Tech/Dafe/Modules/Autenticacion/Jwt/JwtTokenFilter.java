package com.Tech.Dafe.Modules.Autenticacion.Jwt;

import com.Tech.Dafe.Modules.Autenticacion.Service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    //private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtProvider jwtProvider;
    
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(req);
            if(token != null && jwtProvider.validateToken(token)){
                String email = jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            logger.error("Fallo en el método DoFilter " + e.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    /*private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }*/

    private String getToken(HttpServletRequest request){
    String header = request.getHeader("Authorization");

    if(header != null && header.startsWith("Bearer ")) {
        return header.substring(7); // elimina "Bearer "
    }

        return null;
    }

}
