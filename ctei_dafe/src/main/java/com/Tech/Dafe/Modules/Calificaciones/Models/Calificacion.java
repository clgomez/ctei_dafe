package com.Tech.Dafe.Modules.Calificaciones.Models;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "calificacion")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float calificacionDescripcion;
    private Float calificacionFinal;
    private Float calificacionJustificacion;
    private Float calificacionPoblacionObjetivo;
    private Float calificacionPresupuesto;
    private Float calificacionTitulo;
    private String comentario;
    private String estado;
    private String fechaCalificacion;
    private Float resultadoEsperado;
    private Long idUsuario;
    private Long IdInscripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SemaforoEnum semaforo;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "semaforo_id")
    private Semaforo semaforos;

    @JsonIgnore
    @OneToMany(mappedBy = "calificacion")
    private List<Notificacion> notificaciones;

    @ManyToOne
    @JoinColumn(name = "id_arbol_de_problemas", nullable = true)
    private ArbolDeProblemas arbolDeProblemas;

    @ManyToOne
    @JoinColumn(name = "id_causa", nullable = true)
    private Causa causa;

    @ManyToOne
    @JoinColumn(name = "id_fin", nullable = true)
    private Fines fin;


    @ManyToOne
    @JoinColumn(name = "id_arbol_de_objetivos", nullable = true)
    private ArbolDeObjetivos arbolDeObjetivos;

    @ManyToOne
    @JoinColumn(name = "id_efecto", nullable = true)
    private Efecto efecto;

    @ManyToOne
    @JoinColumn(name = "id_medio", nullable = true)
    private Medio medio;


    @ManyToOne
    @JoinColumn(name = "id_cronograma", nullable = true)
    private Cronograma cronograma;

    public Calificacion() {
        this.fechaCalificacion = LocalDateTime.now().toString();
    }

}
