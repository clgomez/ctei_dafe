import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../Models/user.model';
import { AuthService } from '../../Services/auth.service';


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
    estado: '',
    fecha_nacimiento: '',
    tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
    identificacion: '',
    genero: 'Otro',
    ocupacion: 'Estudiante',
    telefono: ''
  };
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit() {
    this.authService.register(this.user)
      .subscribe(
        () => {
          this.router.navigate(['/login']);
          alert('Registro exitoso, inicia sesion')
        },
        error => {
          this.error = error.error.mensaje || 'Ocurrió un error';
          setTimeout(() => {
            this.error = '';
          }, 2000);
        }
      );
  }
  
}
