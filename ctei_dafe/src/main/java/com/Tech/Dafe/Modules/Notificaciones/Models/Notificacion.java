package com.Tech.Dafe.Modules.Notificaciones.Models;

import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estado;
    private String fechaNotificacion;
    private String mensaje;
    private boolean leida;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    @ManyToOne
    @JoinColumn(name = "id_calificacion")
    private Calificacion calificacion;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_inscripcion")
    private Inscripcion inscripcion;

}