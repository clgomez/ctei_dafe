package com.Tech.Dafe.Modules.Enums.TipoIdentificacion.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "tipo_de_identificacion")
public class TipoIdentificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public TipoIdentificacion() {
    }

    public TipoIdentificacion(String nombre) {
        this.nombre = nombre;
    }
}
