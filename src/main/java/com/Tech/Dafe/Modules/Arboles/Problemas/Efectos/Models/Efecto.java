package com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models;

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
@Table(name = "efecto")
public class Efecto {

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
    @ManyToOne
    @JoinColumn(name = "id_arbol_de_problemas")
    private ArbolDeProblemas arbolProblemas;
}