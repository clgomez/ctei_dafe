import { Component, OnInit } from '@angular/core';
import { Convocatoria } from '@app/Models/convocatoria.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { User } from '@app/Models/user.model';
import { AuthService } from '@app/Services/auth.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import swal from 'sweetalert2';

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
              private datePipe: DatePipe,
              private router: Router) { }
  ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarConvocatorias();

   }

 cargarConvocatorias(): void {

  this.convocatoriaService
      .getConvocatorias().subscribe({
        next: (response) => {

          const convocatoriasArr = Array.isArray(response)
            ? response
            : [];

          this.convocatorias = convocatoriasArr.map(p => {

            p.fechaInicio = p.fechaInicio
              ? this.datePipe.transform(
                  p.fechaInicio,
                  'dd/MM/yyyy'
                ) || ''
              : '';

            p.fechaFin = p.fechaFin
              ? this.datePipe.transform(
                  p.fechaFin,
                  'dd/MM/yyyy'
                ) || ''
              : '';

            return {
              ...p
            };
          });
        },

        error: (err) => {
          console.error(err);
          swal.fire({
            icon: 'error',
            title: 'Error',
            text:
              err.error?.mensajes?.[0]
              || 'Error al consultar convocatorias'
          });
        }
      });
}

   eliminarConvocatoria(convocatoria: Convocatoria): void {

         swal.fire({
              title: 'Está seguro?',
              text: `¿Seguro que desea eliminar la convocatoria: ${convocatoria.descripcion}?`,
              icon: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33',
              confirmButtonText: 'Si, eliminar!',
              cancelButtonText: 'No, cancelar!',
              customClass: {
                confirmButton: 'btn btn-primary',
                cancelButton: 'btn btn-danger',
        
              },
              buttonsStyling: false,
              reverseButtons: true
            } as any).then((result) => {
              if (result.value) {
                 this.convocatoriaService.deleteConvocatoria(convocatoria.id).subscribe({
                    next: (data: any) => {
                      this.convocatorias = this.convocatorias.filter(conv => conv !== convocatoria)
                      swal.fire(
                            'Convocatoria Eliminada!',
                            `Convocatoria: ${convocatoria.descripcion} ${data.mensaje} `,
                            'success'
                          );
                        //this.messageService.add({severity: 'success', summary: 'Success', detail: 'Convocatoria Eliminada'});
                      },
                       error: (err) => {
                          console.error(err);
                          swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text:
                              err.error?.mensajes?.[0]
                              || 'No fue posible eliminar la convocatoria'
                          });
                       }
                  });
              }else
              {
                swal.fire(
                      'Eliminación cancelada!',
                      `Eliminación de la convocatoria: ${convocatoria.descripcion} cancelada`,
                      'info'
                )
              }
            })
  }


   logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
