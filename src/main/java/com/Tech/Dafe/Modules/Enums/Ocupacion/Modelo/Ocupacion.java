package com.Tech.Dafe.Modules.Enums.Ocupacion.Modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Data
@AllArgsConstructor
@Table(name = "ocupacion")
public class Ocupacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Ocupacion() {
    }

    public Ocupacion(String nombre) {
        this.nombre = nombre;
    }


}
