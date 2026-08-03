package com.Tech.Dafe.Modules.Actividades.Actividades.Models;

import java.util.List;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Models.Fin;
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
    private Fin fin;

    @ManyToOne
    @JoinColumn(name = "id_causa")
    private Causa causa;

    @ManyToOne
    @JoinColumn(name = "id_medio")
    private Medio medio;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL)
    private List<Cronograma> cronogramas;

}
