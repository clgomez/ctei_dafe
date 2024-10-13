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
        if(this.authService.hasRole("ROL_INVESTIGADOR"))
            this.router.navigate(['/homeinvestigador']);
          else  this.router.navigate(['/home']);
        alert('Exito al iniciar sesion')
      },
      error: (error)=> {
        console.log(error);
        this.error = error.error.mensaje || 'Ocurrió un error';
      }
     });
 }

}
