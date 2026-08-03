import { Component, OnInit } from '@angular/core';
import { User } from '@app/Models/user.model';
import { AuthService } from '@app/Services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { CausaService } from '@app/Services/causa.service';
import { EfectoService } from '@app/Services/efecto.service';
import { MedioService } from '@app/Services/medio.service';
import { FinService } from '@app/Services/fin.service';
import { Actividad } from '@app/Models/actividad.model';
import { ActividadService } from '@app/Services/actividad.service';
import { CronogramaService } from '@app/Services/cronograma.service';
import { forkJoin, Observable, of, firstValueFrom  } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import swal from 'sweetalert2';
import { formatDate } from '@angular/common';


interface MarcoLogico {
    causaId?: number;
    efectoId?: number;
    medioId?: number;
    finId?: number;
    descripcionCML: string;
    id?: number;
    nombreActividad : string;
    descripcionActividad: string;
  }


class ActividadYCML{

    id?: number;
    nombreActividad: string;
    descripcionActividad: string;
    fechaInicio?: Date | string;   
    fechaFin?: Date | string;
    causaDescripcion: string;
    efectoDescripcion: string;
    medioDescripcion: string;
    finDescripcion: string;
}

@Component({
  selector: 'app-actividadesinvest',
  templateUrl: './actividadesinvest.component.html',
  styleUrls: ['./actividadesinvest.component.css']
})
export class ActividadesInvestComponent implements OnInit {
  currentUser: User | null = null;

  public proyectos: Proyecto[];

  public actividad: Actividad;

  public actividades: Actividad[] = [];

  public actividadYCML: ActividadYCML;

  public actividadesYCML: ActividadYCML[] = [];

  public proyecto: Proyecto = new Proyecto;

  public titulo: string = 'Cronograma de Actividades';

  actividadYCMarcoLogicoSeleccionado: ActividadYCML | null = null;

  public visibleDetalleActividad: boolean = false;

  constructor(

    private proyectoService: ProyectoService,
    private actividadService: ActividadService,
    private causaService: CausaService,
    private efectoService: EfectoService,
    private medioService: MedioService,
    private finService: FinService,
    private cronogramaService: CronogramaService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute) { }


  ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarProyecto();


  }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.proyectoService.getProyecto(id).subscribe(
            (proyecto) =>
                        {   this.proyecto = proyecto
                            this.cargarActividadesPorIdProyecto();
                        })

      }
    })
  }

  cargarActividadesPorIdProyecto():void
  {
    this.actividadService.getActividadesPorIdProyecto(this.proyecto.id).subscribe({
        next: (actividades) =>
        { this.actividades =  Array.isArray(actividades) ? actividades : [];
            console.log(this.actividades);

          this.cargarNombresComponentesMarcoLogico();
            console.log(actividades.length);
          if(this.actividades.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar actividades en la bd", err.error.mensaje,"error");
        }
        });

  }

  get existenActividades(): boolean {
    return this.actividades && this.actividades.length > 0;
  }

  cargarCronogramasPorActividad(): void {

  this.actividadesYCML.forEach(actividad => {

    this.cronogramaService
      .getCronogramaPorIdActividad(actividad.id)
      .subscribe({
        next: cronograma => {

          if (!cronograma) return;

          //actividad.cronogramaId = cronograma.id;
          console.log('contenido cronograma', cronograma);

          if (cronograma.fechaInicio) {
            actividad.fechaInicio = new Date(cronograma.fechaInicio + 'T00:00:00');
            actividad.fechaInicio = formatDate(actividad.fechaInicio, 'dd/MM/yyyy', 'en-US');
          }

          if (cronograma.fechaFin) {
            actividad.fechaFin = new Date(cronograma.fechaFin + 'T00:00:00');
            actividad.fechaFin = formatDate(actividad.fechaFin, 'dd/MM/yyyy', 'en-US');
          }

          
        },
        error: () => {
          // No existe cronograma → modo creación
        }
      });
  });
}

  cargarNombresComponentesMarcoLogico() {
    const observables: Observable<MarcoLogico[]>[] = this.actividades.map(actividad => {

      const causa$ = this.causaService.getCausa(actividad.causaId).pipe(
        //tap(causa => console.log('Causa:', causa)),
        map(causa => ({ causaId: actividad.causaId, descripcionCML: causa.descripcion, nombreActividad: actividad.nombre, descripcionActividad: actividad.descripcion, id: actividad.id} as MarcoLogico))
      );

      const efecto$ = this.efectoService.getEfecto(actividad.efectoId).pipe(
        //tap(efecto => console.log('Efecto:', efecto)),
        map(efecto => ({ efectoId: actividad.efectoId, descripcionCML: efecto.descripcion, nombreActividad: actividad.nombre, descripcionActividad: actividad.descripcion, id: actividad.id} as MarcoLogico))
      );

      const medio$ = this.medioService.getMedio(actividad.medioId).pipe(
        //tap(medio => console.log('Medio:', medio)),
        map(medio => ({ medioId: actividad.medioId, descripcionCML: medio.descripcion, nombreActividad: actividad.nombre, descripcionActividad: actividad.descripcion, id: actividad.id} as MarcoLogico))
      );

      const fin$ = this.finService.getFin(actividad.finId).pipe(
        //tap(fin => console.log('Fin:', fin)),
        map(fin => ({ finId: actividad.finId, descripcionCML: fin.descripcion, nombreActividad: actividad.nombre, descripcionActividad: actividad.descripcion, id: actividad.id} as MarcoLogico))
      );

      return forkJoin([causa$, efecto$, medio$, fin$]);
    });

    forkJoin(observables).pipe(
    tap(results => {
      results.forEach(result => {
        this.actividadYCML = new ActividadYCML();

        result.forEach(item => {

            this.actividadYCML.id = item.id;
            this.actividadYCML.nombreActividad = item.nombreActividad;
            this.actividadYCML.descripcionActividad = item.descripcionActividad;

          if (item.causaId) {
            this.actividadYCML.causaDescripcion = item.descripcionCML;
            //console.log(item.nombreCML);
          }
          if (item.efectoId) {
            this.actividadYCML.efectoDescripcion = item.descripcionCML;
            //console.log(item.nombreCML);
          }
          if (item.medioId) {
            this.actividadYCML.medioDescripcion = item.descripcionCML;
            //console.log(item.nombreCML);
          }
          if (item.finId) {
            this.actividadYCML.finDescripcion = item.descripcionCML;
            //console.log(item.nombreCML);
          }

          //console.log(this.actividadYCML);

        });
        this.actividadesYCML.push(this.actividadYCML);
      });

      console.log(this.actividadesYCML);
      //this.establecerFechasInicioFinPorActividad();
    }),
    catchError(error => {
      console.error('Error al cargar componentes del marco lógico', error);
      return of([]); // Retorna un observable vacío o maneja el error de otra forma
    })
  ).subscribe({
      next: () => {
        //AHORA sí, cuando YA existe actividadesYCML
        this.cargarCronogramasPorActividad();
      }
    });
  }

  
  public generarActividadesDelProyecto()
  {
   
    console.log('id del proyecto:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/formactividadesinvest', this.proyecto.id]);

  }


  public eliminarActividadesDelProyecto(): void {
    swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea eliminar las actividades del proyecto?`,
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
    } as any).then(async (result) => {
      if (result.value) {
        // eliminando actividades del proyecto
          try {
              // Itera sobre cada actividad en el arreglo y actualiza cada actividad de forma secuencial
              for (const actividadYCML of this.actividadesYCML) {
                try {
      
                  console.log('id actividad',actividadYCML.id);
      
                  this.actividad = this.actividades.find(actividad => actividad.id === actividadYCML.id);
                  //console.log('actividad',this.actividad);
      
                  await firstValueFrom(this.actividadService.deleteActividad(this.actividad.id));
                  console.log('Actividad eliminada');
                } catch (err) {
                  console.error(`Error al eliminar actividad ${this.actividad.id}`, err);
                }
              }
              this.actividadesYCML = [];
              this.actividades = [];
              this.cargarActividadesPorIdProyecto();
        
              // Una vez que todas las actividades han sido actualizadas, puedes realizar alguna acción posterior
              console.log('Todas las actividades fueron eliminadas exitosamente');
              swal.fire('Actividades eliminadas',`Actividades del proyecto eliminadas con éxito!`, 'success');
              this.router.navigate(['/investigador/proyectosinvestigador/actividadesinvest', this.proyecto.id]);
      
            } catch (err) {
              console.error('Error al eliminar las actividades de proyecto:', err);
            }
      }else
      {
        swal.fire(
              'Eliminación cancelada!',
              `Eliminación de las actividades del proyecto cancelada`,
              'info'
        )

      }
    })
  }


  mostrarDetalleActividad(actividadYCMarcoLogico: ActividadYCML):void
   {
    this.actividadYCMarcoLogicoSeleccionado = actividadYCMarcoLogico;
    this.visibleDetalleActividad = true;
   }

    public regresarAlArbolDeObjetivos()
  {
    console.log('id del proyecto en actividades:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest', this.proyecto.id]);

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
