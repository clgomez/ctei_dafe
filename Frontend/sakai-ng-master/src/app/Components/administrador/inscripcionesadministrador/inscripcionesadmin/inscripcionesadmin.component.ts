import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Inscripcion } from '@app/Models/inscripcion.model';
import { InscripcionService } from '@app/Services/inscripcion.service';
import { InscripcionProyectoDTO } from '@app/Models/inscripcionproyecto-dto.model';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { NgZone } from '@angular/core';
import { User } from '@app/Models/user.model'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Convocatoria } from '@app/Models/convocatoria.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { DatePipe } from '@angular/common';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import swal from 'sweetalert2';


class InscripcionUPC
{

    inscripcion: Inscripcion;
    nombreUsuario: string;
    proyecto: Proyecto = new Proyecto();
    tituloConvocatoria: string;
    estadoSeleccionado: string;

}

@Component({
  selector: 'app-inscripcionesadmin',
  templateUrl: './inscripcionesadmin.component.html',
  styleUrls: ['./inscripcionesadmin.component.css']
})
export class InscripcionesAdminComponent implements OnInit {
  currentUser: User | null = null;

  public inscripciones: Inscripcion[] = [];

  public inscripcionProyectoDTO: InscripcionProyectoDTO = new InscripcionProyectoDTO();

  public inscripcionUPC: InscripcionUPC;

  public inscripcionesUPC: InscripcionUPC[] = [];

  public visibleDetalleProyecto: boolean = false;

  proyectoSeleccionado: Proyecto | null = null;

  validationError = '';
  globalError ='';

  constructor(
    private inscripcionService: InscripcionService,
    private usuarioService: UsuarioService,
    private proyectoService: ProyectoService,
    private convocatoriaService: ConvocatoriaService,
    private errorHandlerService: ErrorHandlerService,
    private authService: AuthService,
    private router: Router,
    private datePipe: DatePipe,
    private cd: ChangeDetectorRef,
    private ngZone: NgZone
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarInscripciones();

  }
 
   cargarInscripciones():void {
      
    this.inscripcionService.getInscripciones().subscribe({
          next: (response) => {
            const inscripcionesArr = Array.isArray(response)
              ? response
              : [];

            this.inscripciones = inscripcionesArr.map(p => {
  
              p.fechaInscripcion = p.fechaInscripcion
                ? this.datePipe.transform(
                    p.fechaInscripcion,
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

            this.cargarUsuarioProyectoConvocatoria();

          },
  
          error: (err) => {
            console.error(err);
            swal.fire({
              icon: 'error',
              title: 'Error',
              text:
                err.error?.mensajes?.[0]
                || 'Error al consultar inscripciones'
            });
          }
        });
      }


  cargarUsuarioProyectoConvocatoria(): void
  {

    this.inscripciones.forEach(inscripcion => {

        this.inscripcionUPC = new InscripcionUPC();

        this.inscripcionUPC.inscripcion = inscripcion;

        this.inscripcionUPC.estadoSeleccionado = '';

        this.inscripcionesUPC.push(this.inscripcionUPC);


    });

    this.inscripcionesUPC.forEach(inscripcionUPC => {


      this.usuarioService.getUsuario(inscripcionUPC.inscripcion.usuarioId).subscribe({
        next: (usuario) => {
            inscripcionUPC.nombreUsuario = `${usuario.nombre} ${usuario.apellidos}`;
        },
        error: (err) => {
          console.error(`Error al obtener el usuario ${inscripcionUPC.inscripcion.usuarioId}`, err);
        }
      });


        this.proyectoService.getProyecto(inscripcionUPC.inscripcion.proyectoId).subscribe({
            next: (proyecto) => {

                inscripcionUPC.proyecto = proyecto;

                 proyecto.fechaCreacion = proyecto.fechaCreacion
                ? this.datePipe.transform(
                    proyecto.fechaCreacion,
                    'dd/MM/yyyy HH:mm:ss'
                  ) || ''
                : '';

                proyecto.fechaActualizacion = proyecto.fechaActualizacion
                ? this.datePipe.transform(
                    proyecto.fechaActualizacion,
                    'dd/MM/yyyy HH:mm:ss'
                  ) || ''
                : '';


            },
            error: (err) => {
              console.error(`Error al obtener el proyecto ${inscripcionUPC.inscripcion.proyectoId}`, err);
            }
          });


      this.convocatoriaService.getConvocatoria(inscripcionUPC.inscripcion.convocatoriaId).subscribe({
        next: (convocatoria) => {
            inscripcionUPC.tituloConvocatoria = convocatoria.titulo;
        },
        error: (err) => {
          console.error(`Error al obtener la convocatoria ${inscripcionUPC.inscripcion.convocatoriaId}`, err);
        }
      });

    });


  }

  public aprobarInscripcionProyecto(inscripcionUPC: InscripcionUPC): void{

    if(inscripcionUPC.estadoSeleccionado != ''){
    console.log('id inscripcion:', inscripcionUPC.inscripcion.id)

    this.inscripcionService.getInscripcion(inscripcionUPC.inscripcion.id)
    .subscribe( (inscripcionProyectoDTO) =>
        {   this.inscripcionProyectoDTO = inscripcionProyectoDTO
            console.log("fecha inscripcion",inscripcionUPC.inscripcion.fechaInscripcion)
            console.log("fecha actualizacion",inscripcionUPC.inscripcion.fechaActualizacion)
            console.log("proyecto id",this.inscripcionProyectoDTO.proyectoId)
            console.log("convocatoria id",this.inscripcionProyectoDTO.convocatoriaId)
            console.log("estado",this.inscripcionProyectoDTO.estado)

            this.inscripcionProyectoDTO.estado = inscripcionUPC.estadoSeleccionado;

            swal.fire({
                title: 'Está seguro?',
                text: `¿Confirma cambiar el estado de inscripción a ${inscripcionUPC.estadoSeleccionado}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, confirmar!',
                cancelButtonText: 'No, cancelar!',
                customClass: {
                  confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger',

                },
                buttonsStyling: false,
                reverseButtons: true
              } as any).then((result) => {
                if (result.value) {

            this.inscripcionService.updateInscripcion(inscripcionUPC.inscripcion.id, this.inscripcionProyectoDTO)
                .subscribe({
                    next: (data: any) => {

                   const index = this.inscripcionesUPC.findIndex(i => i.inscripcion.id === inscripcionUPC.inscripcion.id);
                    if (index !== -1) {
                      this.inscripcionesUPC[index].inscripcion.estado = inscripcionUPC.estadoSeleccionado;
                      this.inscripcionesUPC[index].inscripcion.fechaActualizacion =  
                      this.datePipe.transform(data.inscripcion.fechaActualizacion, 'dd/MM/yyyy HH:mm:ss') || '';
                      swal.fire('Estado de inscripción actualizada',`Estado de inscripción: se actualizó a 
                               ${inscripcionUPC.estadoSeleccionado} con éxito!`, 'success');
                      inscripcionUPC.estadoSeleccionado = '';
                    }

                    this.ngZone.run(() => {
                        this.cd.detectChanges();
                      });
                  
                    },
                    error: (error) => 
                    {

                       console.log('Error completo:', error); // IMPORTANTE para debug

                      this.validationError = '';
                      this.globalError ='';

                      console.log('mensajeGlobal');
                      console.log(error.mensajeGlobal);
                      
                      this.validationError = this.errorHandlerService.procesarError(
                        error,
                        null,
                        'Error al actualizar la inscripción del proyecto.'
                      );

                      this.globalError = error.mensajeGlobal || '';

                      console.log('Mensaje final:');
                      console.log(this.validationError);

                      swal.fire({
                        icon: 'error',
                        title: 'No fue posible realizar la inscripción',
                        text: this.validationError,
                        footer: this.globalError ? `<b>${this.globalError}</b>` : '',
                        confirmButtonText: 'Aceptar'
                      });
                      
                    }
                });
            }else
             {
                swal.fire(
               'Actualización cancelada!',
               `Actualización del estado de inscripción a: ${inscripcionUPC.estadoSeleccionado} cancelado`,
               'info'
               )
             }
        })

    })


    }

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
