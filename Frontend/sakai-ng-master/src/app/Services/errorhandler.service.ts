import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  procesarError(error: any, form?: any, 
                mensajePorDefecto: string = 'Error inesperado.'): string {

    // BUSINESS_ERROR
    if (
      error.error?.codigo === 'BUSINESS_ERROR' &&
      Array.isArray(error.error.mensajes)
    ) {

      let mensaje = '';

      error.error.mensajes.forEach((err: any) => {

        if (form?.controls?.[err.campo]) {

          form.controls[err.campo].setErrors({
            backend: err.mensaje
          });

          form.controls[err.campo].markAsTouched();

          //console.log(err.campo, form.controls[err.campo].errors);

        } else 
        {
          mensaje += err.mensaje + ' | ';
          
        }

      });

      if(mensaje)
      {
          mensaje = mensaje.replace(/\s\|\s$/, '');
          
      }
      else
      {
          mensaje = 'Existen errores de validación en el formulario.';
      }  
      return mensaje;

    }
       

      //valida errores VALIDATION_ERROR(DTO), INVALID_FORMAT, INTERNAL_ERROR
     // Backend devuelve lista
    if (Array.isArray(error.error?.mensajes)) {
      console.log('hola como estas 1');
      return error.error.mensajes.join(' | ');
           
    }

    // valida errores de iniciar sesion: 
    //credenciales invalidas, cuenta de usuario no activa
    if (error.error?.mensaje) {
      console.log('hola como estas 2');
      return error.error.mensaje;
    }

    // mensaje global del interceptor
    /*if (error.mensajeGlobal) {
      return error.mensajeGlobal;
    }
   */
    return mensajePorDefecto;

  }
}