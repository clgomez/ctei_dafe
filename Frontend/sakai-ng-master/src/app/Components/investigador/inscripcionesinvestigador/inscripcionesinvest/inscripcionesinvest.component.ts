import { Component, OnInit } from '@angular/core';
import { Inscripcion } from 'src/app/Models/inscripcion.model';
import { InscripcionService } from 'src/app/Services/inscripcion.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';

@Component({
  selector: 'app-inscripcionesinvest',
  templateUrl: './inscripcionesinvest.component.html',
  styleUrls: ['./inscripcionesinvest.component.css']
})
export class InscripcionesInvestComponent implements OnInit {
  currentUser: User | null = null;

  inscripciones: Inscripcion[];

  constructor(
    private inscripcionService: InscripcionService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.inscripcionService.getInscripciones().subscribe({
        next: (respose) =>
        { //console.log(respose);
            if (Array.isArray(respose)) {
                this.inscripciones = respose;
                if(this.inscripciones.length == 0)
                    //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
                    console.log('lista vacia');
            }else {
                console.error("no es arreglo: ",respose);
              }

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        }
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
