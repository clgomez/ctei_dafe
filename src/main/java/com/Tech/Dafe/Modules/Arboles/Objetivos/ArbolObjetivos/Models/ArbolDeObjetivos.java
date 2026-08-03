package com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models;

import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Models.Fin;
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
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private SemaforoEnum semaforo = SemaforoEnum.Pendiente;

    @ManyToOne
    @JoinColumn(name = "semaforo_id")
    private Semaforo semaforos;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_proyecto", unique = true)
    private Proyecto proyecto;

    //@OneToMany(mappedBy = "arbolDeObjetivos")
    @OneToMany(mappedBy = "arbolDeObjetivos", cascade = CascadeType.ALL)
    private List<Medio> medios;

    //@OneToMany(mappedBy = "arbolDeObjetivos")
    @OneToMany(mappedBy = "arbolDeObjetivos", cascade = CascadeType.ALL)
    private List<Fin> fines;
}