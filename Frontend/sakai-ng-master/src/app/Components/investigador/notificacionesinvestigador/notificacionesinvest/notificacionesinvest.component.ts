import { Component, OnInit } from '@angular/core';
import { Notificacion } from 'src/app/Models/notificacion.model';
import { NotificacionService } from 'src/app/Services/notificacion.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';

@Component({
  selector: 'app-notificacionesinvest',
  templateUrl: './notificacionesinvest.component.html',
  styleUrls: ['./notificacionesinvest.component.css']
})
export class NotificacionesInvestComponent implements OnInit {
  currentUser: User | null = null;

  public usuario: User | null;

  notificaciones: Notificacion[];

  constructor(
    private notificacionService: NotificacionService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);
    console.log("username", this.currentUser.username);
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
                    (user => {this.usuario = user, this.cargarNotificacionesPorUsuario()});


  }

  public cargarNotificacionesPorUsuario():void
  {
    console.log("id usuario", this.usuario.id);

    this.notificacionService.getNotificacionesPorUsuario(this.usuario.id).subscribe({
        next: (respose) =>
        { this.notificaciones =  Array.isArray(respose) ? respose : [];
          if(this.notificaciones.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
         }
        });

  }

  eliminarNotificacion(notificacion: Notificacion): void {

    this.notificacionService.deleteNotificacion(notificacion.id).subscribe(
      response => {
        this.notificaciones = this.notificaciones.filter(notif => notif !== notificacion)
        /*swal.fire(
          'Proyecto Eliminado!',
          `Proyecto ${proyecto.titulo} eliminado con éxito.`,
          'success'
        )*/
          //this.messageService.add({severity: 'success', summary: 'Success', detail: 'Proyecto Eliminado'});
      }
    )

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
