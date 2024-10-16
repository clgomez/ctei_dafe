package com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models;

import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Enums.Semaforo.Models.Semaforo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "causa")
public class Causa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float calificacion = 0.0f;
    private String nombre;
    private String comentario = "No definido";

    @Enumerated(EnumType.STRING)
    private SemaforoEnum semaforo = SemaforoEnum.Pendiente;

    @ManyToOne
    @JoinColumn(name = "semaforo_id")
    private Semaforo semaforos;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_arbol_de_problemas")
    private ArbolDeProblemas arbolDeProblemas;

}