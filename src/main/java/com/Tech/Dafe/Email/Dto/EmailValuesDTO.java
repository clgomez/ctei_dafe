package com.Tech.Dafe.Email.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailValuesDTO {

    private String mailFrom;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String mailTo;
    
    private String subject;
    private String username;
    private String tokenPassword;



}