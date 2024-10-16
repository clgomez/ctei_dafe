package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Models;

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

    private String estado;
    private String fecha_asignacion;
    private String fecha_actualizacion;

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