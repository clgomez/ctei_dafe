package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models;

import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Models.Inscripcion;
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

    private String descripcion;
    private String estado;
    private String fecha_fin;
    private String fecha_inicio;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToOne(mappedBy = "convocatoria", cascade = CascadeType.ALL)
    private Inscripcion inscripcion;

}