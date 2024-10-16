package com.Tech.Dafe.Modules.Enums.Semaforo.Models;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Table(name = "semaforo")
public class Semaforo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArbolDeObjetivos> arbolesDeObjetivos = new HashSet<>();

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fines> fines = new HashSet<>();

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Medio> medios = new HashSet<>();

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArbolDeProblemas> arbolDeProblemas = new HashSet<>();

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Causa> causas = new HashSet<>();

    @OneToMany(mappedBy = "semaforo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Efecto> efectos = new HashSet<>();

    public Semaforo() {
    }

    public Semaforo(String nombre) {
        this.nombre = nombre;
    }
}
