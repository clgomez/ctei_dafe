package com.Tech.Dafe.Response;

public class CampoError {
    private String campo;
    private String mensaje;

    public CampoError(String campo, String mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }

    public String getCampo() { return campo; }
    public String getMensaje() { return mensaje; }
}