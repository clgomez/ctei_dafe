import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '@app/Models/user.model'; 
import { AuthService } from '@app/Services/auth.service'; 
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user: User = {
    nombre: '',
    apellidos: '',
    username: '',
    email: '',
    password: '',
    direccion: '',
    estado: 'ACTIVO',
    fechaNacimiento: '',
    tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
    identificacion: '',
    genero: 'Otro',
    ocupacion: 'Estudiante',
    telefono: ''
  };
  
 
  constructor(
    private authService: AuthService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router
  ) { }

  validationError = '';
  globalError = '';
  minFechaNacimiento: string = '';
  maxFechaNacimiento: string = '';
  minAge = 9;
  
ngOnInit() {
  const today = new Date();

  // Edad mínima (ya lo tienes)
  const minAgeDate = new Date(
    today.getFullYear() - this.minAge,
    today.getMonth(),
    today.getDate()
  );
  this.maxFechaNacimiento = minAgeDate.toISOString().split('T')[0];

  // Edad máxima (ej: 80 años)
  const maxAge = 80;
  const maxAgeDate = new Date(
    today.getFullYear() - maxAge,
    today.getMonth(),
    today.getDate()
  );
  this.minFechaNacimiento = maxAgeDate.toISOString().split('T')[0];
}

isFormatoFechaValido(): boolean {
  if (!this.user.fechaNacimiento) return true;

  const formatoISO = /^\d{4}-\d{2}-\d{2}$/;

  if (!formatoISO.test(this.user.fechaNacimiento)) {
    return false;
  }

  const [year, month, day] = this.user.fechaNacimiento.split('-').map(Number);

  // Crear fecha SIN problemas de zona horaria
  const fecha = new Date(year, month - 1, day);

  return (
    fecha.getFullYear() === year &&
    fecha.getMonth() === month - 1 &&
    fecha.getDate() === day
  );
}

  isMinAgeValid(): boolean {
  if (!this.user.fechaNacimiento) return true;

  const [year, month, day] = this.user.fechaNacimiento.split('-').map(Number);
  const inputDate = new Date(year, month - 1, day);

  const today = new Date();

  const minAgeDate = new Date(
    today.getFullYear() - this.minAge,
    today.getMonth(),
    today.getDate()
  );

  return inputDate <= minAgeDate;
}

  isMaxAgeValid(): boolean {
  if (!this.user.fechaNacimiento) return true;

  const [year, month, day] = this.user.fechaNacimiento.split('-').map(Number);
  const inputDate = new Date(year, month - 1, day);

  const minDate = new Date(this.minFechaNacimiento);

  return inputDate >= minDate;
}


validarCampos(): boolean {

  const errores: string[] = [];

  const nombrePattern = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$/;
  const usernamePattern = /^[A-Za-z0-9_]{4,20}$/;
  const emailPattern = /^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$/;
  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
  const telefonoPattern = /^\d{10}$/;
  const identificacionPattern = /^\d{6,15}$/;
  const estadoPattern = /^(ACTIVO|NO ACTIVO)$/;

  // Nombre
  if (!this.user.nombre) {
    errores.push('El nombre es obligatorio');
  } else if (!nombrePattern.test(this.user.nombre)) {
    errores.push('El nombre debe contener solo letras y tener entre 2 y 50 caracteres');
  }

  // Apellidos
  if (!this.user.apellidos) {
    errores.push('Los apellidos son obligatorios');
  } else if (!nombrePattern.test(this.user.apellidos)) {
    errores.push('Los apellidos deben contener solo letras y tener entre 2 y 50 caracteres');
  }

  // Username
  if (!this.user.username) {
    errores.push('El nombre de usuario es obligatorio');
  } else if (!usernamePattern.test(this.user.username)) {
    errores.push('El nombre de usuario debe tener entre 4 y 20 caracteres y solo letras, números o guión bajo _');
  }

  // Email
  if (!this.user.email) {
    errores.push('El correo electrónico es obligatorio');
  } else if (!emailPattern.test(this.user.email)) {
    errores.push('Formato de correo electrónico inválido. Ejemplo válido: usuario@dominio.com');
  }

  // Password
  if (!this.user.password) {
    errores.push('La contraseña es obligatoria');
  } else if (!passwordPattern.test(this.user.password)) {
    errores.push('La contraseña debe tener mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial');
  }

  // Teléfono
  if (!this.user.telefono) {
    errores.push('El teléfono es obligatorio');
  } else if (!telefonoPattern.test(this.user.telefono)) {
    errores.push('El teléfono debe contener exactamente 10 dígitos');
  }

  // Dirección
  if (!this.user.direccion) {
    errores.push('La dirección es obligatoria');
  } else if (this.user.direccion.length < 5) {
    errores.push('La dirección debe tener al menos 5 caracteres');
  }

  // Tipo identificación
  if (!this.user.tipoIdentificacion) {
    errores.push('El tipo de identificación es obligatorio');
  }

  // Identificación
  if (!this.user.identificacion) {
    errores.push('La identificación es obligatoria');
  } else if (!identificacionPattern.test(this.user.identificacion)) {
    errores.push('La identificación debe contener entre 6 y 15 dígitos');
  }

  // Fecha nacimiento
  if (!this.user.fechaNacimiento) {
    errores.push('La fecha de nacimiento es obligatoria');
  } else if (!this.isFormatoFechaValido()) {
    errores.push('Formato de fecha inválido (usa yyyy-MM-dd)');
  }else if (!this.isMinAgeValid()) {
    errores.push('Debes tener al menos 9 años para registrarte');
  } else if (!this.isMaxAgeValid()) {
    errores.push('La edad no puede ser mayor a 80 años para registrarse');
  }

  // Género
  if (!this.user.genero) {
    errores.push('El género es obligatorio');
  }

  // Ocupación
  if (!this.user.ocupacion) {
    errores.push('La ocupación es obligatoria');
  }

  // Estado
  if (!this.user.estado) {
    errores.push('El estado del usuario es obligatorio');
  } else if (!estadoPattern.test(this.user.estado)) {
    errores.push('El estado del usuario solo puede ser ACTIVO o NO ACTIVO');
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

    // Marcar todos los campos como tocados
    if (form.invalid) {
      Object.values(form.controls).forEach((control: any) => {
        //control.markAsTouched();
      });
      //return;
    }

    //console.log('Fecha cruda:', this.user.fechaNacimiento);
    //console.log('Tipo:', typeof this.user.fechaNacimiento);
   
    // Validar campos obligatorios

    this.validationError = '';
    this.globalError ='';

    if (!this.validarCampos()) {
       if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

   
    this.authService.register(this.user, 'investigador')
    .subscribe({
      next: (data: any) => {
        //console.log('prueba: ', data);
        alert(data.mensaje);
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
          'No se pudo registrar el usuario.'
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

    }//fin error
    });

  }

}

