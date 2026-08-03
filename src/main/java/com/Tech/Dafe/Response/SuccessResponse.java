package com.Tech.Dafe.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {

    private String codigo;
    private String mensaje;
}