import { Component, OnInit } from '@angular/core';
import { Convocatoria } from 'src/app/Models/convocatoria.model';
import { ConvocatoriaService } from 'src/app/Services/convocatoria.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';

@Component({
  selector: 'app-convocatoriasinvest',
  templateUrl: './convocatoriasinvest.component.html',
  styleUrls: ['./convocatoriasinvest.component.css']
})
export class ConvocatoriasInvestComponent implements OnInit {
  currentUser: User | null = null;

  convocatorias: Convocatoria[];

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.convocatoriaService.getConvocatorias().subscribe({
        next: (respose) =>
        { this.convocatorias = respose;
          if(this.convocatorias.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });

  }






  isAdmin(): boolean {
    return this.authService.hasRole('ROL_ADMIN');
  }

  isUser(): boolean {
    return this.authService.hasRole('ROL_USER');
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
