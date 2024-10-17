package com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fechaInscripcion;
    private String estado = "PENDIENTE";

    private String fechaActualizacion;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_proyecto", unique = true)
    private Proyecto proyecto;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_convocatoria", unique = true)
    private Convocatoria convocatoria;
}
