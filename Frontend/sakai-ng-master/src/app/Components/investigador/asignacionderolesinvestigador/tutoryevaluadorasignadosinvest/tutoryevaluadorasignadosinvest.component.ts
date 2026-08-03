import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { AsignacionDeRoles } from '@app/Models/asignacionderoles.model';
import { AsignacionDeRolesService } from '@app/Services/asignacionderoles.service';
import { ProyectoService } from '@app/Services/proyecto.service';
import { DatePipe } from '@angular/common';
import { forkJoin} from 'rxjs';
import { map} from 'rxjs/operators';
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

interface Proyecto {
    proyectoId: number;
    titulo: string;
}

interface Usuario {
    id: number;
    investigador: string;
    tutor: string;
    evaluador: string;
}

type Resultado = Proyecto | Usuario;

// Verifica si el objeto es de tipo Proyecto
function esProyecto(obj: Resultado): obj is Proyecto {
    return (obj as Proyecto).proyectoId !== undefined;
}

// Verifica si el objeto es de tipo Usuario
function esUsuario(obj: Resultado): obj is Usuario {
    return (obj as Usuario).investigador !== undefined;
}




@Component({
  selector: 'app-tutoryevaluadorasignadosinvest',
  templateUrl: './tutoryevaluadorasignadosinvest.component.html',
  styleUrls: ['./tutoryevaluadorasignadosinvest.component.css']
})
export class TutorYEvaluadorAsignadosInvestComponent implements OnInit {
  currentUser: User | null = null;

  public usuario: User | null;

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
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
    (user => {this.usuario = user, this.cargarAsignacionesDeRolesPorIdUsuarioInvestigador()});


  }

  cargarAsignacionesDeRolesPorIdUsuarioInvestigador():void {
         
       this.asignacionDeRolesService.getAsignacionesDeRolesPorIdUsuarioInvestigador(this.usuario.id).subscribe({
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


public cargarNombresDeProyectosYUsuarios() {
    // Primero, obtenemos las solicitudes para los proyectos.
    const proyectos$ = this.asignacionesDeRoles.map(asignacionDeRoles =>
        this.proyectoService.getProyecto(asignacionDeRoles.proyectoId).pipe(
            map(proyecto => ({
                proyectoId: asignacionDeRoles.proyectoId,
                titulo: proyecto.titulo
            }))
        )
    );

    // Luego, obtenemos las solicitudes para los usuarios (Investigador, Tutor y Evaluador)
    const usuarios$ = this.asignacionesDeRoles.map(asignacionDeRoles =>
        forkJoin({
            investigador: this.usuarioService.getUsuario(asignacionDeRoles.usuarioInvestigadorId),
            tutor: this.usuarioService.getUsuario(asignacionDeRoles.usuarioTutorId),
            evaluador: this.usuarioService.getUsuario(asignacionDeRoles.usuarioEvaluadorId)
        }).pipe(
            map(({ investigador, tutor, evaluador }) => ({
                id: asignacionDeRoles.id,
                investigador: `${investigador.nombre} ${investigador.apellidos}`,
                tutor: `${tutor.nombre} ${tutor.apellidos}`,
                evaluador: `${evaluador.nombre} ${evaluador.apellidos}`
            }))
        )
    );

    // Unimos ambas solicitudes usando forkJoin
    forkJoin([...proyectos$, ...usuarios$]).subscribe(results => {
        // Los primeros 'n' resultados son proyectos
        const proyectos = results.slice(0, this.asignacionesDeRoles.length);
        // Los siguientes 'n' resultados son usuarios
        const usuarios = results.slice(this.asignacionesDeRoles.length);

        // Asignamos la información de los proyectos y los usuarios a las asignaciones de roles
        this.asignacionesDeRolesNomPITE = this.asignacionesDeRoles.map((asignacionDeRoles, index) => {
            const proyecto = proyectos.find(p => esProyecto(p) && p.proyectoId === asignacionDeRoles.proyectoId);
            const usuario = usuarios[index];

            return {
                id: asignacionDeRoles.id,
                fechaAsignacion: asignacionDeRoles.fechaAsignacion,
                fechaActualizacion: asignacionDeRoles.fechaActualizacion,
                estado: asignacionDeRoles.estado,
                proyectoId: asignacionDeRoles.proyectoId,
                tituloProyecto: proyecto && esProyecto(proyecto) ? proyecto.titulo : 'Sin título',
                usuarioInvestigadorId: asignacionDeRoles.usuarioInvestigadorId,
                nombreUsuarioInvestigador: usuario && esUsuario(usuario) ? usuario.investigador : 'Sin nombre', // Type guard para acceder al investigador
                usuarioTutorId: asignacionDeRoles.usuarioTutorId,
                nombreUsuarioTutor: usuario && esUsuario(usuario) ? usuario.tutor : 'Sin nombre',
                usuarioEvaluadorId: asignacionDeRoles.usuarioEvaluadorId,
                nombreUsuarioEvaluador: usuario && esUsuario(usuario) ? usuario.evaluador : 'Sin nombre'
            };
        });
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

public salirTutorYEvaluadorAsignadosInvestigador():void{
    console.log('pulso salir de tutor y evaluador asignados del investigador');

   this.router.navigate(['/']);


  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  /*cargarNombresDeProyectosYUsuarios() {
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
*/


}
