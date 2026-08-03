import { Component, OnInit } from '@angular/core';
import { EmailValuesDTO } from '../models/email-values-dto'; 
import { EmailPasswordService } from '@app/Services/email.service';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

@Component({
  selector: 'app-send-email',
  templateUrl: './send-email.component.html',
  styleUrls: ['./send-email.component.css'],
})
export class SendEmailComponent implements OnInit {
  mailTo: string = '';
  dto: EmailValuesDTO = new EmailValuesDTO('');
  //message: string = '';
  //isError: boolean = false;
  validationError = '';
  globalError = '';
  success: string = '';

  constructor(
    private emailPasswordService: EmailPasswordService,
    private errorHandlerService : ErrorHandlerService

  ) {}

  ngOnInit() {}

  validarCampos(): boolean {

    const errores: string[] = [];

    // Username
    if (!this.mailTo) {
      errores.push('El nombre de usuario es obligatorio');
    }

      // Mostrar todos los errores
      if (errores.length > 0) {
        console.log('Errores de validación:');
        errores.forEach(err => console.log('- ' + err));

        // Opcional: mostrar todos juntos en el HTML
        this.validationError = errores.join(' | ');
        return false;
      }

      return true;
  }

  onSendEmail(form: any): void {

    // Validar campos obligatorios
   if (!this.validarCampos()) {
       if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }
  
    this.dto = new EmailValuesDTO(this.mailTo);
    this.emailPasswordService.sendEmail(this.dto).subscribe({
    next: (data) => {
      this.success = data.mensaje;
      console.log(this.success);
       if (this.success) {
              setTimeout(() => {
                this.success = '';
              }, 4000);
        }

      //this.isError = false;
    },
    error: (error) => {

      console.log('Error completo:', error); // IMPORTANTE para debug

        this.validationError = '';
        this.globalError ='';

        console.log('mensajeGlobal');
        console.log(error.mensajeGlobal);
        
        this.validationError = this.errorHandlerService.procesarError(
          error,
          form,
          'Ocurrió un error al enviar el correo.'
        );
        
        this.globalError = error.mensajeGlobal;

         console.log('Mensaje final:');
         console.log(this.validationError);

        // limpiar mensaje global
        if (this.validationError) {
          setTimeout(() => {
            this.validationError = '';
          }, 4000);
        }

         if (this.globalError) {
          setTimeout(() => {
            this.globalError = '';
          }, 4000);
        }
    

    }
 
    });
  }
  
}
