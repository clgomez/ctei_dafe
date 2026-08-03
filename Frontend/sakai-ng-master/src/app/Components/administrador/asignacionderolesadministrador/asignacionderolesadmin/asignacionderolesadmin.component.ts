import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { AsignacionDeRoles } from '@app/Models/asignacionderoles.model';
import { AsignacionDeRolesService } from '@app/Services/asignacionderoles.service';
import { ProyectoService } from '@app/Services/proyecto.service';
import { DatePipe } from '@angular/common';
import swal from 'sweetalert2';


class AsignacionDeRolesNomPITE
{
    id?: number;
    fechaAsignacion: string;
    fechaActualizacion: string;
    estado: string;
    proyectoId: number;
    tituloProyecto: string;
    usuarioInvestigadorId: number;
    nombreUsuarioInvestigador: string;
    usuarioTutorId: number;
    nombreUsuarioTutor: string;
    usuarioEvaluadorId: number;
    nombreUsuarioEvaluador: string;

}


@Component({
  selector: 'app-asignacionderolesadmin',
  templateUrl: './asignacionderolesadmin.component.html',
  styleUrls: ['./asignacionderolesadmin.component.css']
})
export class AsignacionDeRolesAdminComponent implements OnInit {
  currentUser: User | null = null;


  public asignacionesDeRoles: AsignacionDeRoles[] = [];

  public asignacionDeRolesNomPITE: AsignacionDeRolesNomPITE;

  public asignacionesDeRolesNomPITE: AsignacionDeRolesNomPITE[] = [];

  //nombresProyectos: { [key: number]: string } = {};
  //nombresUsuarios: { [key: number]: string } = {};

  constructor(
    private asignacionDeRolesService: AsignacionDeRolesService,
    private proyectoService: ProyectoService,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private datePipe: DatePipe, 
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarAsignacionesDeRoles();


  }

  cargarAsignacionesDeRoles():void {
        
      this.asignacionDeRolesService.getAsignacionesDeRoles().subscribe({
            next: (response) => {
              const asignacionesDeRolesArr = Array.isArray(response)
                ? response
                : [];
  
              this.asignacionesDeRoles = asignacionesDeRolesArr.map(p => {
    
                p.fechaAsignacion = p.fechaAsignacion
                  ? this.datePipe.transform(
                      p.fechaAsignacion,
                      'dd/MM/yyyy HH:mm:ss'
                    ) || ''
                  : '';
    
                p.fechaActualizacion = p.fechaActualizacion
                  ? this.datePipe.transform(
                      p.fechaActualizacion,
                      'dd/MM/yyyy HH:mm:ss'
                    ) || ''
                  : '';
    
                return {
                  ...p
                };
              });
  
             this.cargarNombresDeProyectosYUsuarios();
  
            },
    
            error: (err) => {
              console.error(err);
              swal.fire({
                icon: 'error',
                title: 'Error',
                text:
                  err.error?.mensajes?.[0]
                  || 'Error al consultar asignaciones de roles '
              });
            }
          });
        }


  cargarNombresDeProyectosYUsuarios() {
    this.asignacionesDeRoles.forEach(asignacionDeRoles => {

        this.asignacionDeRolesNomPITE = new AsignacionDeRolesNomPITE();

        this.asignacionDeRolesNomPITE.id = asignacionDeRoles.id;
        this.asignacionDeRolesNomPITE.fechaAsignacion = asignacionDeRoles.fechaAsignacion;
        this.asignacionDeRolesNomPITE.fechaActualizacion = asignacionDeRoles.fechaActualizacion;
        this.asignacionDeRolesNomPITE.estado = asignacionDeRoles.estado;
        this.asignacionDeRolesNomPITE.proyectoId = asignacionDeRoles.proyectoId
        this.asignacionDeRolesNomPITE.usuarioInvestigadorId = asignacionDeRoles.usuarioInvestigadorId;
        this.asignacionDeRolesNomPITE.usuarioTutorId = asignacionDeRoles.usuarioTutorId;
        this.asignacionDeRolesNomPITE.usuarioEvaluadorId = asignacionDeRoles.usuarioEvaluadorId;

        this.asignacionesDeRolesNomPITE.push(this.asignacionDeRolesNomPITE);


    });

    this.asignacionesDeRolesNomPITE.forEach(asignacionDeRolesNomPITE => {

        this.proyectoService.getProyecto(asignacionDeRolesNomPITE.proyectoId).subscribe({
            next: (proyecto) => {

                asignacionDeRolesNomPITE.tituloProyecto = proyecto.titulo;

            },
            error: (err) => {
              console.error(`Error al obtener el proyecto ${asignacionDeRolesNomPITE.proyectoId}`, err);
            }
          });


      // Obtener el nombre y apellido del usuario Investigador
      this.usuarioService.getUsuario(asignacionDeRolesNomPITE.usuarioInvestigadorId).subscribe({
        next: (usuario) => {
              asignacionDeRolesNomPITE.nombreUsuarioInvestigador = `${usuario.nombre} ${usuario.apellidos}`;
        },
        error: (err) => {
          console.error(`Error al obtener el usuario Investigador ${asignacionDeRolesNomPITE.usuarioInvestigadorId}`, err);
        }
      });

      // Obtener el nombre y apellido del usuario Tutor
      this.usuarioService.getUsuario(asignacionDeRolesNomPITE.usuarioTutorId).subscribe({
        next: (usuario) => {
            asignacionDeRolesNomPITE.nombreUsuarioTutor = `${usuario.nombre} ${usuario.apellidos}`;
        },
        error: (err) => {
          console.error(`Error al obtener el usuario Tutor ${asignacionDeRolesNomPITE.usuarioTutorId}`, err);
        }
      });

      // Obtener el nombre y apellido del usuario Evaluador
      this.usuarioService.getUsuario(asignacionDeRolesNomPITE.usuarioEvaluadorId).subscribe({
        next: (usuario) => {
              asignacionDeRolesNomPITE.nombreUsuarioEvaluador = `${usuario.nombre} ${usuario.apellidos}`;
        },
        error: (err) => {
          console.error(`Error al obtener el usuario Evaluador ${asignacionDeRolesNomPITE.usuarioEvaluadorId}`, err);
        }
      });

    });
  }


  eliminarAsignacionDeRoles(asignacionDeRolesNomPITE: AsignacionDeRolesNomPITE): void {

    swal.fire({
               title: 'Está seguro?',
               text: `¿Seguro que desea eliminar esta asignación de roles?`,
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
                    this.asignacionDeRolesService.deleteAsignacionDeRoles(asignacionDeRolesNomPITE.id).subscribe(
                    {
                      next: (data) => 
                      {
                      this.asignacionesDeRolesNomPITE = this.asignacionesDeRolesNomPITE.filter(AsignDeRolesNomPITE => AsignDeRolesNomPITE !== asignacionDeRolesNomPITE)
                      swal.fire(
                        'Asignación de Roles  Eliminada!',
                        `Asignación de Roles eliminada con éxito.`,
                        'success'
                      )
                        //this.messageService.add({severity: 'success', summary: 'Success', detail: 'Proyecto Eliminado'});
                     },
                     error: (err) => 
                     {
                        console.error(err);
                        swal.fire({
                          icon: 'error',
                          title: 'Error',
                          text:
                            err.error?.mensajes?.[0]
                            || 'Error al eliminar la asignación de roles'
                        });
                     }

                    });
                  }else
                  {
                    swal.fire(
                          'Eliminación cancelada!',
                          `Eliminación de la asignación de roles cancelada`,
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
