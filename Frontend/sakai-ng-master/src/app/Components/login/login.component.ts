import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { LoginUser } from '@app/Models/login-user';  
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginUser: LoginUser = { email: '', password: '' };
  validationError = '';
  globalError ='';
  success: string = '';
 
  constructor(
    private authService: AuthService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
  ) {}


 validarCampos(): boolean {

  const errores: string[] = [];  

   // Email
  if (!this.loginUser.email) {
    errores.push('El email es obligatorio');
  } 

   // Password
  if (!this.loginUser.password) {
    errores.push('El password es obligatorio');
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
 onSubmit(form: any) {

  this.globalError = '';
  this.success = '';

  // Validar campos obligatorios
    if (!this.validarCampos()) {
       if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

    this.authService.login(this.loginUser).subscribe({

      next: () => {

        console.log("username", this.loginUser.email);

        // obtener datos del usuario por email
        this.authService.getUserbyEmail(this.loginUser.email).subscribe({

          next: (user) => {

            console.log("estado del usuario:", user.estado);

              this.success = 'Inicio de sesión exitoso';
              console.log('mensaje exitoso:', this.success);

              alert('Éxito al iniciar sesión');

              this.router.navigate(['/welcome']);

          },

          error: (err) => {
                console.log('Error completo:', err); 
                
                this.validationError = '';
                this.globalError ='';

                console.log('mensajeGlobal');
                console.log(err.mensajeGlobal);
                
                this.validationError = this.errorHandlerService.procesarError(
                  err,
                  form
                );

                this.globalError = err.mensajeGlobal;

                console.log('Mensaje final:');
                console.log(this.validationError);
                
                  // limpiar mensaje global
                  if (this.globalError) {
                    setTimeout(() => {
                      this.globalError = '';
                    }, 4000);
                  }
              }
            });
      },

     error: (error) => {

        console.log('Error completo:', error); 

        this.validationError = '';
        this.globalError = '';

        console.log('mensajeGlobal');
        console.log(error.mensajeGlobal);
        
        this.validationError = this.errorHandlerService.procesarError(
          error,
          form,
          'No se pudo iniciar sesión. Intente nuevamente.'
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