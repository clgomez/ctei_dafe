package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "arbol_de_objetivos")
public class ArbolDeObjetivos {

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

    @OneToMany(mappedBy = "arbolDeObjetivos")
    private List<Medio> medios;

    @OneToMany(mappedBy = "arbolDeObjetivos")
    private List<Fines> fines;
}