package com.Tech.Dafe.Modules.Calificaciones.Models;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.EstadoCalificacion;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "calificacion")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float calificacionFinal;
         
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaCreacion;
      
    @Enumerated(EnumType.STRING)
    private EstadoCalificacion estado;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private SemaforoEnum semaforo;

    private Float notaTutor;

    private Float notaEvaluador;

    private Long idTutorHistorico;

    private Long idEvaluadorHistorico;

    private String nombreTutorHistorico;

    private String nombreEvaluadorHistorico;


    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

  
    @JsonIgnore
    @OneToMany(mappedBy = "calificacion")
    private List<Notificacion> notificaciones;

    @ManyToOne
    @JoinColumn(name = "id_usuario_investigador")
    private Usuario usuarioInvestigador;

    @ManyToOne
    @JoinColumn(name = "id_usuario_tutor")
    private Usuario usuarioTutor;

    @ManyToOne
    @JoinColumn(name = "id_usuario_evaluador")
    private Usuario usuarioEvaluador;

    @OneToMany(
            mappedBy = "calificacion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RubroCalificacion> rubros = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }    

}
