package com.Tech.Dafe.Modules.Proyecto.Proyectos.Models;

import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models.AsignacionDeRoles;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private String estado;
    private String fechaActualizacion;
    private String fechaCreacion;
    private String justificacion;
    private String observaciones;
    private String poblacionObjetivo;
    private String presupuesto;
    private String resultadosEsperados;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "proyecto")
    private List<Inscripcion> inscripciones;

    @OneToOne(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private ArbolDeObjetivos arbolDeObjetivos;

    @OneToOne(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private ArbolDeProblemas ArbolDeProblemas;


    @JsonIgnore
    @OneToMany(mappedBy = "proyecto")
    private List<Actividad> actividades;

    @JsonIgnore
    @OneToMany(mappedBy = "proyecto")
    private List<Calificacion> calificaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "proyecto")
    private List<AsignacionDeRoles> asignacionesDeRoles;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_cronograma")
    private Cronograma cronograma;

}