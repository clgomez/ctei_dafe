package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models;

import java.time.LocalDateTime;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "asignacion_de_roles")
public class AsignacionDeRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaAsignacion;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaActualizacion;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario_tutor")
    private Usuario usuarioTutor;

    @ManyToOne
    @JoinColumn(name = "id_usuario_evaluador")
    private Usuario usuarioEvaluador;

    @ManyToOne
    @JoinColumn(name = "id_usuario_investigador")
    private Usuario usuarioInvestigador;


}