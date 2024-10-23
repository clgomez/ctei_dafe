import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import { Proyecto } from 'src/app/Models/proyecto.model';
import { ProyectoService } from 'src/app/Services/proyecto.service';


@Component({
  selector: 'app-proyectosadmin',
  templateUrl: './proyectosadmin.component.html',
  styleUrls: ['./proyectosadmin.component.css']
})
export class ProyectosAdminComponent implements OnInit {
  currentUser: User | null = null;

  public visibleDetalleProyecto: boolean = false;

  proyectos: Proyecto[];

  proyectoSeleccionado: Proyecto | null = null;

  constructor(
    private proyectoService: ProyectoService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.proyectoService.getProyectos().subscribe({
        next: (respose) =>
        { this.proyectos = respose;
            console.log(respose.length);
          if(this.proyectos.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });

  }

  mostrarDetalleProyecto(proyecto: Proyecto):void
   {
    this.proyectoSeleccionado = proyecto;
    this.visibleDetalleProyecto = true;
   }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
