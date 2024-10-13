package com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models;

import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
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
@Table(name = "arbol_de_problemas")
public class ArbolDeProblemas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float calificacion = 0.0f;
    private String descripcion;
    private String nombre;
    private String comentario = "No definido";

    @Enumerated(EnumType.STRING)
    private SemaforoEnum semaforo = SemaforoEnum.Pendiente;

    @ManyToOne
    @JoinColumn(name = "semaforo_id")
    private Semaforo semaforos;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_proyecto", unique = true)
    private Proyecto proyecto;

    @OneToMany(mappedBy = "arbolDeProblemas")
    private List<Causa> causas;

    @OneToMany(mappedBy = "arbolProblemas")
    private List<Efecto> efectos;

}