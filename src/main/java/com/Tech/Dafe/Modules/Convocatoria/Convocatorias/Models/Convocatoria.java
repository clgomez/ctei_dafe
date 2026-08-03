package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models;

import java.time.LocalDate;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
import com.Tech.Dafe.Utils.NormalizarTitulo.TituloNormalizer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "convocatoria")
public class Convocatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    @Column(unique = true)
    private String tituloNormalizado;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToOne(mappedBy = "convocatoria", cascade = CascadeType.ALL)
    private Inscripcion inscripcion;

    @PrePersist
    @PreUpdate
    private void generarTituloNormalizado() {
        this.tituloNormalizado =
                TituloNormalizer.normalizar(this.titulo);
    }

}