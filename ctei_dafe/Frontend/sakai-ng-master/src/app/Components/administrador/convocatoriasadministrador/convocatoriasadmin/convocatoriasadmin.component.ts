import { Component, OnInit } from '@angular/core';
import { Convocatoria } from 'src/app/Models/convocatoria.model';
import { ConvocatoriaService } from 'src/app/Services/convocatoria.service';
import { User } from 'src/app/Models/user.model';
import { AuthService } from 'src/app/Services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-convocatoriasadmin',
  templateUrl: './convocatoriasadmin.component.html',
  styleUrls: ['./convocatoriasadmin.component.css']
})
export class ConvocatoriasAdminComponent implements OnInit {
  currentUser: User | null = null;

  convocatorias: Convocatoria[];

  constructor(private convocatoriaService: ConvocatoriaService,
              private authService: AuthService,
              private router: Router) { }
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

   eliminarConvocatoria(convocatoria: Convocatoria): void {

        this.convocatoriaService.deleteConvocatoria(convocatoria.id).subscribe(
          response => {
            this.convocatorias = this.convocatorias.filter(conv => conv !== convocatoria)
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
