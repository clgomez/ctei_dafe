import { Component, OnInit } from '@angular/core';
import { Convocatoria } from '@app/Models/convocatoria.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { InscripcionProyectoDTO } from '@app/Models/inscripcionproyecto-dto.model';
import { InscripcionService } from '@app/Services/inscripcion.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { DatePipe } from '@angular/common';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-inscripcionproyectoinvest',
  templateUrl: './inscripcionproyectoinvest.component.html',
  styleUrls: ['./inscripcionproyectoinvest.component.css']
})
export class InscripcionProyectoInvestComponent implements OnInit {
  currentUser: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public inscripcionProyectoDTO: InscripcionProyectoDTO = new InscripcionProyectoDTO();

  convocatorias: Convocatoria[];

  validationError = '';
  globalError ='';

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private inscripcionService: InscripcionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private proyectoService: ProyectoService,
    private datePipe: DatePipe,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarProyecto();

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

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) => this.proyecto = proyecto)

      }
    })
  }

  public inscribirProyecto(convocatoria: Convocatoria): void{

    this.inscripcionProyectoDTO.estado = 'PENDIENTE';
    this.inscripcionProyectoDTO.proyectoId = this.proyecto.id;
    this.inscripcionProyectoDTO.convocatoriaId = convocatoria.id;

    swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea inscribirse a la convocatoria: ${convocatoria.descripcion}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, inscribirse!',
      cancelButtonText: 'No, cancelar!',
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger',

      },
      buttonsStyling: false,
      reverseButtons: true
    } as any).then((result) => {
      if (result.value) {
      this.inscripcionService.createInscripcion(this.inscripcionProyectoDTO)
        .subscribe(
        {
            next: (data: any) => {
              this.router.navigate(['/investigador/inscripcionesinvestigador/inscripcionesinvest']);
            
                swal.fire(
                          'Nueva Inscripción a convocatoria!',
                          `Inscripción a convocatoria: ${convocatoria.descripcion} creada con éxito.`,
                          'success'
                        );
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
                  'Errror al inscribir el proyecto.'
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
        })
      }else
      {
        swal.fire(
              'Inscripción a convocatoria cancelada!',
              `Inscripción a la convocatoria: ${convocatoria.descripcion} cancelada`,
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
