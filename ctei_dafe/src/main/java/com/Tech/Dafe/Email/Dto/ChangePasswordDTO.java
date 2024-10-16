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
public class ChangePasswordDTO {

    @NotBlank
    private String password;

    @NotBlank
    private String confirmarPassword;

    @NotBlank
    private String tokenPassword;
}
