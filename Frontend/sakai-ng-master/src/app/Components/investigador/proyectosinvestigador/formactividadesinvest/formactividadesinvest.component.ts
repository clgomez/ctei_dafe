import { Component, OnInit } from '@angular/core';
import { User } from '@app/Models/user.model';
import { AuthService } from '@app/Services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { ArbolDeObjetivos } from '@app/Models/arboldeobjetivos.model';
import { ArbolDeObjetivosService } from '@app/Services/arboldeobjetivos.service';
import { Causa } from '@app/Models/causa.model';
import { CausaService } from '@app/Services/causa.service';
import { Efecto } from '@app/Models/efecto.model';
import { EfectoService } from '@app/Services/efecto.service';
import { Medio } from '@app/Models/medio.model';
import { MedioService } from '@app/Services/medio.service';
import { Fin } from '@app/Models/fin.model';
import { FinService } from '@app/Services/fin.service';
import { Actividad } from '@app/Models/actividad.model';
import { ActividadService } from '@app/Services/actividad.service';
import { Cronograma } from '@app/Models/cronograma.model';
import { CronogramaService } from '@app/Services/cronograma.service';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { firstValueFrom } from 'rxjs';
import swal from 'sweetalert2';
import { formatDate } from '@angular/common';
import moment from 'moment';

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

    fechaInicio?: Date | string;   // 👈 acepta ambos
    fechaFin?: Date | string;

    minDateInicio?: Date;
    minDateFin?: Date;

    cronogramaId?: number; 

    erroresFechas = {
    inicio: '',
    fin: ''
   };

   fechasValidas: boolean = true;

    causaDescripcion: string;
    efectoDescripcion: string;
    medioDescripcion: string;
    finDescripcion: string;
}

@Component({
  selector: 'app-formactividadesinvest',
  templateUrl: './formactividadesinvest.component.html',
  styleUrls: ['./formactividadesinvest.component.css']
})

export class FormActividadesInvestComponent implements OnInit {

  currentUser: User | null = null;

  public actividad: Actividad = new Actividad;

  public actividades: Actividad[] = [];

  public actividadesCargadas: Actividad[] = [];

  public actividadYCML: ActividadYCML;

  public actividadesYCML: ActividadYCML[] = [];

  public cronograma: Cronograma = new Cronograma;

  public cronogramas: Cronograma[] = [];

  public proyecto: Proyecto = new Proyecto;
  
  public titulo: string = 'Programar Actividades';

  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();
  
  public arbolDeObjetivos: ArbolDeObjetivos = new ArbolDeObjetivos();

  public causa: Causa = new Causa();

  public efecto: Efecto = new Efecto();
  
  public medio: Medio = new Medio();

  public fin: Fin = new Fin();

  public causasArbolDeProblemas: Causa[] = [];

  public efectosArbolDeProblemas: Efecto[] = [];

  public mediosArbolDeObjetivos: Medio[] = [];

  public finesArbolDeObjetivos: Fin[] = [];

  // Aquí declaras la variable para el placeholder dinámico
  public placeholderFechaInicio: string = 'dd/mm/yyyy';
  public placeholderFechaFin: string = 'dd/mm/yyyy';

    
  constructor(
              private proyectoService: ProyectoService,
              private arbolDeProblemasService: ArbolDeProblemasService,
              private arbolDeObjetivosService: ArbolDeObjetivosService,
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
      let idProyecto = params['id']
      if(idProyecto){
        console.log('id', idProyecto);
        this.proyectoService.getProyecto(idProyecto).subscribe(
            (proyecto) =>
                        {   this.proyecto = proyecto
                            this.cargarProyectoYMarcoLogico(idProyecto);
                            this.cargarActividadesPorIdProyecto();
                                      
                        })

      }
    })
  }

  cargarActividadesPorIdProyecto():void
  {
    this.actividadService.getActividadesPorIdProyecto(this.proyecto.id).subscribe({
        next: (actividades) =>
        { this.actividadesCargadas =  Array.isArray(actividades) ? actividades : [];
            console.log(this.actividadesCargadas);
            
            console.log(actividades.length);
          if(this.actividadesCargadas.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar actividades en la bd", err.error.mensaje,"error");
        }
        });
  }


  cargarCronogramasPorActividad(): void {

  this.actividadesYCML.forEach(actividad => {

     if (!actividad.id) {
        return;
    }

    this.cronogramaService
      .getCronogramaPorIdActividad(actividad.id)
      .subscribe({
        next: (cronograma) => {

          if (!cronograma) return;

          actividad.cronogramaId = cronograma.id;

          if (cronograma.fechaInicio) {
            actividad.fechaInicio = new Date(cronograma.fechaInicio + 'T00:00:00');
            //actividad.placeholderFechaInicio = formatDate(actividad.fechaInicio, 'dd/MM/yy', 'en-US');
            actividad.minDateFin = this.addOneDay(new Date(actividad.fechaInicio));
          }

          if (cronograma.fechaFin) {
            actividad.fechaFin = new Date(cronograma.fechaFin + 'T00:00:00');
            //actividad.placeholderFechaFin = formatDate(actividad.fechaFin, 'dd/MM/yy', 'en-US');
          }

          this.validarFechasActividad(actividad);
        },
        error: (e) => {
          console.log('Error al consultar cronograma', e);
        }
      });
  });
}

  validarFechasActividad(actividad: ActividadYCML): boolean {
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);

  actividad.erroresFechas = { inicio: '', fin: '' };
  actividad.fechasValidas = true;

  if (!actividad.fechaInicio || !actividad.fechaFin) {
    actividad.fechasValidas = false;
    return false;
  }

  const inicio = new Date(actividad.fechaInicio);
  const fin = new Date(actividad.fechaFin);

  if (inicio < hoy) {
    actividad.erroresFechas.inicio = 'La fecha de inicio no puede ser anterior a hoy.';
    actividad.fechasValidas = false;
  }

   if (fin <= hoy) {
      actividad.erroresFechas.fin = 'La fecha de finalización debe ser posterior a hoy.';
      actividad.fechasValidas = false;
    }

    if (inicio.getTime() === fin.getTime()) {
      actividad.erroresFechas.fin = 'La fecha de inicio y la fecha de finalización no pueden ser iguales.';
      actividad.fechasValidas = false;
    }

  if (fin <= inicio) {
    actividad.erroresFechas.fin = 'La fecha final debe ser posterior a la fecha inicial.';
    actividad.fechasValidas = false;
  }

  return actividad.fechasValidas;
}

  private cargarProyectoYMarcoLogico(idProyecto: number): void {

  this.proyectoService.getProyecto(idProyecto).subscribe({
    next: proyecto => {
      this.proyecto = proyecto;

      forkJoin({
        arbolProblemas: this.arbolDeProblemasService
          .getArbolDeProblemasPorIdProyecto(idProyecto),

        arbolObjetivos: this.arbolDeObjetivosService
          .getArbolDeObjetivosPorIdProyecto(idProyecto)
      }).subscribe({
        next: ({ arbolProblemas, arbolObjetivos }) => {

          if (!arbolProblemas || !arbolObjetivos) {
            swal.fire(
              'Error',
              'El proyecto no tiene árboles completos',
              'error'
            );
            return;
          }

          this.arbolDeProblemas = arbolProblemas;
          this.arbolDeObjetivos = arbolObjetivos;

          // SOLO AQUÍ
          this.cargarComponentesMarcoLogico();
        }
      });
    }
  });
}

  cargarComponentesMarcoLogico(): void {

  // RESETEAR SIEMPRE
  this.causasArbolDeProblemas = [];
  this.efectosArbolDeProblemas = [];
  this.mediosArbolDeObjetivos = [];
  this.finesArbolDeObjetivos = [];

  forkJoin({
    causas: this.causaService.getCausasPorIdArbolDeProblemas(this.arbolDeProblemas.id),
    efectos: this.efectoService.getEfectosPorIdArbolDeProblemas(this.arbolDeProblemas.id),
    medios: this.medioService.getMediosPorIdArbolDeObjetivos(this.arbolDeObjetivos.id),
    fines: this.finService.getFinesPorIdArbolDeObjetivos(this.arbolDeObjetivos.id)
  }).subscribe({
    next: ({ causas, efectos, medios, fines }) => {

      this.causasArbolDeProblemas = causas ?? [];
      this.efectosArbolDeProblemas = efectos ?? [];
      this.mediosArbolDeObjetivos = medios ?? [];
      this.finesArbolDeObjetivos = fines ?? [];

      console.log('Causas:', this.causasArbolDeProblemas.length);
      console.log('Efectos:', this.efectosArbolDeProblemas.length);
      console.log('Medios:', this.mediosArbolDeObjetivos.length);
      console.log('Fines:', this.finesArbolDeObjetivos.length);

      //AHORA SÍ, cuando todo está cargado
      this.verificarYCargarActividades();
    },
    error: err => {
      console.error('Error cargando marco lógico', err);
    }
  });
}

      // Método para verificar y cargar actividades
    public verificarYCargarActividades(): void {

      // SI YA EXISTEN EN BD
      if (this.actividadesCargadas.length > 0) {
        this.actividades = this.actividadesCargadas;
        this.cargarNombresComponentesMarcoLogico();
        return;
      }

      // SI NO EXISTEN, CREARLAS
      //if(this.actividades.length == 0 )
      //{
        if (
        this.causasArbolDeProblemas.length > 0 &&
        this.efectosArbolDeProblemas.length > 0 &&
        this.mediosArbolDeObjetivos.length > 0 &&
        this.finesArbolDeObjetivos.length > 0
      ) {
        this.cargarActividades(); // Carga actividades si todos los arreglos tienen elementos
    
      } else {
        console.log('Uno o más arreglos están vacíos, no se pueden crear actividades.');
        console.log(this.causasArbolDeProblemas);
        console.log(this.efectosArbolDeProblemas);
        console.log(this.mediosArbolDeObjetivos);
        console.log(this.finesArbolDeObjetivos);
      }
   //}else swal.fire("Ya existen actividades creadas");
  }
  
    cargarActividades():void
    {
  
      for (let i = 0; i < Math.min(this.causasArbolDeProblemas.length,
                                  this.efectosArbolDeProblemas.length,
                                  this.mediosArbolDeObjetivos.length,
                                  this.finesArbolDeObjetivos.length); i++) {
          const actividad: Actividad = {
            //id: i + 1,
            nombre: `Actividad ${i + 1}`,
            descripcion: `Descripción de la Actividad ${i + 1}`,
            proyectoId: this.proyecto.id,
            causaId: this.causasArbolDeProblemas[i].id!,
            efectoId: this.efectosArbolDeProblemas[i].id!,
            medioId: this.mediosArbolDeObjetivos[i].id!,
            finId: this.finesArbolDeObjetivos[i].id!,
          };
  
          this.actividades.push(actividad);
  
        }
          console.log(this.actividades);
  
          //this.crearActividades();
          this.cargarNombresComponentesMarcoLogico();
  
    }

private async guardarCronogramaPorActividad(actividadYCML: ActividadYCML): Promise<void> {

  if (!this.validarFechasActividad(actividadYCML)) {
    return;
  }

  console.log('actividades hola', this.actividades);
  console.log('actividadYCML hola', actividadYCML);
  console.log('actividades cargadas', this.actividadesCargadas);

  //const actividad = this.actividades.find(a => a.id === actividadYCML.id);

  const cronogramaToSend = {
    id: actividadYCML.cronogramaId,
    fechaInicio: moment(actividadYCML.fechaInicio).format('YYYY-MM-DD'),
    fechaFin: moment(actividadYCML.fechaFin).format('YYYY-MM-DD'),
    actividadId: actividadYCML.id
  };

  try {

    if (actividadYCML.cronogramaId) {
      await firstValueFrom(
        this.cronogramaService.updateCronograma(cronogramaToSend)
      );
      console.log('Cronograma actualizado:', actividadYCML.id);

    } else {
      const res: any = await firstValueFrom(
        this.cronogramaService.createCronograma(cronogramaToSend)
      );
      actividadYCML.cronogramaId = res.cronograma.id;
      console.log('Cronograma creado:', res.cronograma.id);
    }

  } catch (err) {
    console.error('Error guardando cronograma', err);
  }
   
}

public async guardarCronogramas(): Promise<void> {

  if (this.hayFechasInvalidas()) {
    swal.fire('Error', 'Hay fechas inválidas', 'error');
    return;
  }


  if (this.actividades.length === this.actividadesYCML.length) {
  this.actividadesYCML.forEach((ycml, i) => {
    ycml.id = this.actividades[i].id;
  });
}
  

  for (const actividad of this.actividadesYCML) {
    await this.guardarCronogramaPorActividad(actividad);
  }

  /*swal.fire(
    'Cronogramas',
    'Cronogramas guardados correctamente',
    'success'
  );
  */

  //this.router.navigate(['/investigador/proyectosinvestigador/cronogramainvest',this.proyecto.id]);
}
   
  
public async crearActividades(): Promise<void> {
  try {
    const actividadesConIdReal: Actividad[] = [];
    // Itera sobre cada actividad en el arreglo y crea cada actividad de forma secuencial
    for (const actividad of this.actividades) {
    
        const actividadCreada: any = await firstValueFrom(this.actividadService.createActividad(actividad));
        console.log('Actividad creada:', actividadCreada.actividad.id);
        actividadesConIdReal.push(actividadCreada.actividad);
     
    }

    this.actividades = actividadesConIdReal;

    this.guardarCronogramas();

    // Una vez que todas las actividades han sido creadas, puedes realizar alguna acción posterior
    console.log('Todas las actividades fueron creadas exitosamente');
    swal.fire('Nuevas Actividades',`Actividades: creadas con éxito!`, 'success');
    this.router.navigate(['/investigador/proyectosinvestigador/actividadesinvest', this.proyecto.id]);

  } catch (err) {
    console.error('Error al crear actividades:', err);
  }
}

  cargarNombresComponentesMarcoLogico() {

    this.actividadesYCML = [];
    
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


  public async actualizarActividades():Promise<void>
  {
   
    swal.fire({
        title: 'Está seguro?',
        text: `¿Confirma actualizar actividades del proyecto?`,
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
      } as any).then(async (result) => {
        if (result.value) {
 

    try {
        // Itera sobre cada actividad en el arreglo y actualiza cada actividad de forma secuencial
        for (const actividadYCML of this.actividadesYCML) {
          try {

            console.log('id actividad',actividadYCML.id);

            this.actividad = this.actividades.find(actividad => actividad.id === actividadYCML.id);
            console.log('actividad',this.actividad);

            this.actividad.nombre = actividadYCML.nombreActividad;
            this.actividad.descripcion = actividadYCML.descripcionActividad;

            const res: any = await firstValueFrom(this.actividadService.updateActividad(this.actividad));
            console.log('Actividad actualizada:', res.actividad.id);
          } catch (err) {
            console.error(`Error al actualizar actividad ${this.actividad.id}`, err);
          }
        }

        this.guardarCronogramas();
  
        // Una vez que todas las actividades han sido actualizadas, puedes realizar alguna acción posterior
        console.log('Todas las actividades fueron actualizadas exitosamente');
        swal.fire('Actividades actualizadas',`Actividades del proyecto actualizadas con éxito!`, 'success');
        this.router.navigate(['/investigador/proyectosinvestigador/actividadesinvest', this.proyecto.id]);

      } catch (err) {
        console.error('Error al actualizar actividades:', err);
      }

    }else
    {
      swal.fire(
                'Actualizaciones canceladas!',
                `Actualizaciones del proyecto canceladas`,
                'info'
      )
    }
  })
 

}

/*
 formatDate(date: Date | null): string {
    if (!date) return '';

    //return moment(date).format('DD/MM/YYYY');
    return moment(date).format('YYYY-MM-DD');
  }
  */


  // 🔹 Utilidad para sumar 1 día
  private addOneDay(date: Date): Date {
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + 1);
    return newDate;
  }


  /*onDateChangeFechaInicio(event: Date) {
  console.log('Fecha inicio seleccionada:', event);
  this.cronograma.fechaInicio = event;
  if (event) {
      this.minDateFin = this.addOneDay(event);
    }
  console.log(this.validarFechas());
  }

  onDateChangeFechaFin(event: Date) {
    console.log('Fecha fin seleccionada:', event);
    this.cronograma.fechaFin = event;
    console.log(this.validarFechas());
  } */

  onDateChangeFechaInicio(actividad: ActividadYCML) {

  if (actividad.fechaInicio) {
    const inicio = new Date(actividad.fechaInicio);
    actividad.minDateFin = this.addOneDay(inicio);
  }

  this.validarFechasActividad(actividad);
}


  onDateChangeFechaFin(actividad: ActividadYCML) {
    this.validarFechasActividad(actividad);
  }

  public hayFechasInvalidas(): boolean {
  if (!this.actividadesYCML || this.actividadesYCML.length === 0) {
    return true;
  }

  return this.actividadesYCML.some(a => !a.fechasValidas);
}


 public cancelarActividades():void
  {
      swal.fire(
              'Creación cancelada!',
              `Creación de las actividades: cancelada`,
              'info'
              )
    
    this.router.navigate(['/investigador/proyectosinvestigador/actividadesinvest', this.proyecto.id]);
  }

 
   logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {


  }

}