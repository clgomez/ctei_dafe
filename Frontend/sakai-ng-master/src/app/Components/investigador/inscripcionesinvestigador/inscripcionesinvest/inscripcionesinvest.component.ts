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

  public usuario: User | null;

  inscripciones: Inscripcion[];

  constructor(
    private inscripcionService: InscripcionService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    console.log("username", this.currentUser.username);
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
                    (user => {this.usuario = user, this.cargarInscripcionesPorUsuario()});


  }

  public cargarInscripcionesPorUsuario():void
  {
    console.log("id usuario", this.usuario.id);

    this.inscripcionService.getInscripcionesPorUsuario(this.usuario.id).subscribe({
        next: (respose) =>
        { this.inscripciones =  Array.isArray(respose) ? respose : [];
          if(this.inscripciones.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
         }
        });

  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
