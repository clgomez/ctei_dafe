//import { ToastrService } from 'ngx-toastr';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { EmailPasswordService } from '@app/Services/email.service'; 
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import { ChangePasswordDTO } from '../models/change-password-dto'; 


@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent implements OnInit {
  password: string = '';
  confirmPassword: string = '';
  tokenPassword: string = '';

  //message: string = '';
  //isError: boolean = false;

  validationError = '';
  globalError = '';
  success: string = '';

  constructor(
    private emailPasswordService: EmailPasswordService,
    //private toastrService: ToastrService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorHandlerService : ErrorHandlerService
  ) {}

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      this.tokenPassword = params.get('tokenPassword') || '';
    });
  }

validarCampos(): boolean {

  const errores: string[] = [];

  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
  
  // Password
  if (!this.password) {
    errores.push('La contraseña es obligatoria');
  } else if (!passwordPattern.test(this.password)) {
    errores.push('La contraseña debe tener mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial');
  }

   // Confirmar Password
  if (!this.confirmPassword) {
    errores.push('La contraseña de confirmación es obligatoria');
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


  onChangePassword(form: any): void {

    // Validar campos obligatorios
     if (!this.validarCampos()) {
       if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

    //this.message = '';
    //this.isError = false;

    this.validationError = '';
    this.globalError ='';

    if (this.password !== this.confirmPassword) {
          //this.isError = true;
          this.validationError = 'Las contraseñas no coinciden'
          console.log(this.validationError);
          //alert('Las contraseñas no coinciden');
           if (this.validationError) {
            setTimeout(() => {
              this.validationError = '';
            }, 4000);
          }
          return;
    }

    const dto = new ChangePasswordDTO(
      this.password,
      this.confirmPassword,
      this.tokenPassword
    );

    this.emailPasswordService.changePassword(dto).subscribe({
      next: (data) => {
        this.success = data.mensaje; //Cambio exitoso, puedes iniciar sesión//
        console.log(this.success);
        alert(this.success);
        this.router.navigate(['/login']);
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
          'Ocurrió un error al cambiar la contraseña.'
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
