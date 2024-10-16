package com.Tech.Dafe.Modules.Autenticacion.Modelos;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models.AsignacionDeRoles;
import com.Tech.Dafe.Modules.Enums.Genero.Modelo.Genero;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Enums.OcupacionEnum;
import com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo.Ocupacion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Enums.TipoIdentificacionEnum;
import com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models.TipoIdentificacion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.Tech.Dafe.Modules.Enums.Genero.Enums.GeneroEnum;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    //ATRIBUTOS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String direccion;

    @NotNull
    @Column(unique = true)
    private String email;

    private String estado;
    private String fecha_nacimiento;
    private String fecha_registro;

    @NotNull
    @Column(unique = true)
    private String identificacion;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String telefono;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenPasswordExpiry;
    private String tokenPassword;


    //ENUMS

    @NotNull
    @Enumerated(EnumType.STRING)
    private OcupacionEnum ocupacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoIdentificacionEnum tipoIdentificacion;


    //RELACIONES

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_rol"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    private Genero generoEnt;

    @JsonIgnore
    @ManyToOne
    private Ocupacion ocupacionEnti;

    @JsonIgnore
    @ManyToOne
    private TipoIdentificacion tipoIdentificacionent;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Inscripcion> inscripciones;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Notificacion> notificaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Proyecto> proyectos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioTutor")
    private List<AsignacionDeRoles> asignacionesDeRolesTutor;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioEvaluador")
    private List<AsignacionDeRoles> asignacionesDeRolesEvaluador;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioInvestigador")
    private List<AsignacionDeRoles> asignacionesDeRolesInvestigador;

    //CONSTRUCTORES

    public Usuario(String nombre, String apellidos, String direccion, String email, String estado, String fecha_nacimiento, TipoIdentificacionEnum tipoIdentificacion, String identificacion, GeneroEnum genero, OcupacionEnum ocupacion, String username, String password, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.email = email;
        this.estado = estado;
        this.fecha_nacimiento = fecha_nacimiento;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.genero = genero;
        this.ocupacion = ocupacion;
        this.username = username;
        this.password = password;
        this.telefono = telefono;
    }

}