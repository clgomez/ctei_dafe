import { HttpInterceptor, HttpRequest, 
         HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {

         /*if (error.status === 400) {
           return throwError(() => error);
         }*/

       /*  let backend = error.error;

        if (typeof backend === 'string') {
            try {
                backend = JSON.parse(backend);
            } catch {}
        }
        */       


        let mensaje = '';

        switch (error.status) {

          case 0:
            mensaje = 'No hay conexión con el servidor.';
            break;

          case 400:
            mensaje = "Datos inválidos enviados al servidor.";
            break;  

          case 401:
            mensaje = 'No autorizado.';
            break;

          case 403:
            mensaje = 'Acceso denegado.';
            break;

          case 404:
            mensaje = 'Recurso no encontrado.';
            break;

          case 500:
            mensaje = 'Error interno del servidor.';
            break;

          default:
            mensaje = "Ocurrió un error inesperado.";
        }

       return throwError(() => ({
        ...error, mensajeGlobal: mensaje}));

    
      })
    );
  }
}