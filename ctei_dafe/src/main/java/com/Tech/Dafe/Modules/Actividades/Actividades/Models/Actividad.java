package com.Tech.Dafe.Modules.Actividades.Actividades.Models;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actividad")
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_efecto")
    private Efecto efecto;

    @ManyToOne
    @JoinColumn(name = "id_fin")
    private Fines fin;

    @ManyToOne
    @JoinColumn(name = "id_causa")
    private Causa causa;

    @ManyToOne
    @JoinColumn(name = "id_medio")
    private Medio medio;

}
