import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import { AsignacionDeRoles } from 'src/app/Models/asignacionderoles.model';
import { AsignacionDeRolesService } from 'src/app/Services/asignacionderoles.service';


@Component({
  selector: 'app-asignacionderolesadmin',
  templateUrl: './asignacionderolesadmin.component.html',
  styleUrls: ['./asignacionderolesadmin.component.css']
})
export class AsignacionDeRolesAdminComponent implements OnInit {
  currentUser: User | null = null;

  public asignacionesDeRoles: AsignacionDeRoles[];

  constructor(
    private asignacionDeRolesService: AsignacionDeRolesService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.asignacionDeRolesService.getAsignacionesDeRoles().subscribe({
        next: (respose) =>
        { this.asignacionesDeRoles = respose;
            console.log(respose.length);
          if(this.asignacionesDeRoles.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });


  }

  eliminarAsignacionDeRoles(asignacionDeRoles: AsignacionDeRoles): void {

    this.asignacionDeRolesService.deleteAsignacionDeRoles(asignacionDeRoles.id).subscribe(
      response => {
        this.asignacionesDeRoles = this.asignacionesDeRoles.filter(AsignDeRoles => AsignDeRoles !== asignacionDeRoles)
        /*swal.fire(
          'Proyecto Eliminado!',
          `Proyecto ${proyecto.titulo} eliminado con éxito.`,
          'success'
        )*/
          //this.messageService.add({severity: 'success', summary: 'Success', detail: 'Proyecto Eliminado'});
      }
    )

}


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
