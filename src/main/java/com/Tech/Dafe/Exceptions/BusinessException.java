package com.Tech.Dafe.Exceptions;

import com.Tech.Dafe.Response.CampoError;
import java.util.List;

public class BusinessException extends RuntimeException {

    private List<CampoError> errores;

    public BusinessException(List<CampoError> errores) {
        this.errores = errores;
    }

    public List<CampoError> getErrores() {
        return errores;
    }

    public BusinessException(String mensaje) {
        super(mensaje);
        this.errores = List.of(new CampoError("", mensaje));
    }

    public BusinessException(String campo, String mensaje) {
        super(mensaje);
        this.errores = List.of(new CampoError(campo, mensaje));
    }
    
}