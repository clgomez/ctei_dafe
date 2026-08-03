package com.Tech.Dafe.Modules.Actividades.Cronograma.Models;

import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cronograma")
public class Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    //@JoinColumn(name = "id_actividad", nullable = false)
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;
}
