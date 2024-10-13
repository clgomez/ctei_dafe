package com.Tech.Dafe.Modules.Autenticacion.Modelos;

import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPrincipal implements UserDetails {

    private String nombre;
    private String apellidos;
    private String direccion;
    private String email;
    private String estado;
    private String fecha_nacimiento;
    private TipoIdentificacionEnum tipoIdentificacion;
    private String identificacion;
    private String username;
    private String password;
    private String telefono;
    private GeneroEnum genero;
    private OcupacionEnum ocupacion;

    private Collection<? extends GrantedAuthority> authorities;


    public static UsuarioPrincipal build(Usuario usuario) {
        List<GrantedAuthority> authorities =
                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(
                        rol.getRolNombre().name())).collect(Collectors.toList());
        return new UsuarioPrincipal(
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getDireccion(),
                usuario.getEmail(),
                usuario.getEstado(),
                usuario.getFecha_nacimiento(),
                usuario.getTipoIdentificacion(),
                usuario.getIdentificacion(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getTelefono(),
                usuario.getGenero(),
                usuario.getOcupacion(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
