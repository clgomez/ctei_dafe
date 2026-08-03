package com.Tech.Dafe.Modules.Enums.Roles.DTO;

import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class RolDTO {

    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;
}
