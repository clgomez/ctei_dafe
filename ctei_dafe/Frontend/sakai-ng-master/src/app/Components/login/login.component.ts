import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../Services/auth.service';
import { LoginUser } from '../../Models/login-user';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginUser: LoginUser = { email: '', password: '' };
  error = '';
  success = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  onSubmit() {
    this.authService.login(this.loginUser)
    .subscribe({
      next: (response) => {

        /*
        if(this.authService.hasRole('ROL_INVESTIGADOR'))
            this.router.navigate(['/investigador/homeinvestigador']);
        if(this.authService.hasRole('ROL_TUTOR'))
            this.router.navigate(['/tutor/hometutor']);
        if(this.authService.hasRole('ROL_EVALUADOR'))
            this.router.navigate(['/evaluador/homeevaluador']);
        if(this.authService.hasRole('ROL_ADMINISTRADOR'))
            this.router.navigate(['/administrador/homeadministrador']);

        */

        this.router.navigate(['/welcome']);

        alert('Exito al iniciar sesion')
      },
      error: (error)=> {
        console.log(error);
        this.error = error.error.mensaje || 'Ocurrió un error';
      }
     });
 }

}
