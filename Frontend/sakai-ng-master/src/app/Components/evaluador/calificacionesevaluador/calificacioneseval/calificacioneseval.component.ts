import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { firstValueFrom, forkJoin, of } from 'rxjs';
import swal from 'sweetalert2';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model';
import { Usuario } from '@app/Models/usuario.model';
import { UsuarioService } from '@app/Services/usuario.service';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { Calificacion } from '@app/Models/calificacion.model';
import { CalificacionService } from '@app/Services/calificacion.service';
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { ArbolDeObjetivos } from '@app/Models/arboldeobjetivos.model';
import { ArbolDeObjetivosService } from '@app/Services/arboldeobjetivos.service';
import { CausaService } from '@app/Services/causa.service';
import { EfectoService } from '@app/Services/efecto.service';
import { MedioService } from '@app/Services/medio.service';
import { FinService } from '@app/Services/fin.service';
import { EstadoCalificacion } from '@app/Enums/Calificaciones/estadocalificacion.enum';
import { TipoCalificacion } from '@app/Enums/Calificaciones/tipocalificacion.enum';
import { FieldCalificacion } from '@app/Interfaces/Calificaciones/fieldcalificacion.interface';
import { SeccionCalificacion } from '@app/Interfaces/Calificaciones/seccioncalificacion.interface';
import { CalificacionFieldsHelper } from '@app/Helpers/Calificaciones/calificacion-fields.helper'; 
import { CalificacionMapperHelper } from '@app/Helpers/Calificaciones/calificacion-mapper.helper'; 
import { CalificacionUtilsHelper } from '@app/Helpers/Calificaciones/calificacion-utils.helper'; 
import { RolCalificador } from '@app/Enums/Calificaciones/rolcalificador.enum';

class CalificacionNomPIT {

  calificacion: Calificacion = new Calificacion();
  proyecto: Proyecto = new Proyecto();
  nombresapellidosUsuarioInvestigador: string;
  tituloConvocatoria: string;
  nombresapellidosUsuarioTutor: string;
  nombresapellidosUsuarioEvaluador: string;
  fechaCalificacionFormateada: string;
}

@Component({
  selector: 'app-calificacioneseval',
  templateUrl: './calificacioneseval.component.html',
  styleUrls: ['./calificacioneseval.component.css']
})
export class CalificacionesEvalComponent implements OnInit {

  // =====================================================
  // TITULOS
  // =====================================================

  tituloCalificacionFormulacionDelProyecto = 'Calificación Formulación Del Proyecto';
  tituloCalificacionArbolDeProblemas = 'Calificación Árbol De Problemas';
  tituloCalificacionCausasDelArbolDeProblemas = 'Calificación Causas Del Árbol De Problemas';
  tituloCalificacionEfectosDelArbolDeProblemas = 'Calificación Efectos Del Árbol De Problemas';
  tituloCalificacionArbolDeObjetivos = 'Calificación Árbol De Objetivos';
  tituloCalificacionMediosDelArbolDeObjetivos = 'Calificación Medios Del Árbol De Objetivos';
  tituloCalificacionFinesDelArbolDeObjetivos = 'Calificación Fines Del Árbol De Objetivos';

  // =====================================================
  // VARIABLES
  // =====================================================

  currentUser: User | null = null;
  public usuarioEvaluador: Usuario = new Usuario();
  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();
  public arbolDeObjetivos: ArbolDeObjetivos = new ArbolDeObjetivos();
  public visibleDetalleCalificacion: boolean = false;
  public calificaciones: Calificacion[] = [];
  public calificacionesNomPIT: CalificacionNomPIT[] = [];
  public calificacionSeleccionada: Calificacion = new Calificacion();
  public proyectoSeleccionado: Proyecto = new Proyecto();
  public secciones: SeccionCalificacion[] = [];
  public EstadoCalificacion = EstadoCalificacion;

  // =====================================================
  // FIELDS
  // =====================================================

  fieldsFormulacionDelProyecto: FieldCalificacion[] = [

    CalificacionFieldsHelper.crearFieldBase(
      'Título',
      'Título',
      TipoCalificacion.TITULO,
      'titulo'
    ),

    CalificacionFieldsHelper.crearFieldBase(
      'Descripción',
      'Descripción',
      TipoCalificacion.DESCRIPCION,
      'descripcion'
    ),

    CalificacionFieldsHelper.crearFieldBase(
      'Población Objetivo',
      'Población objetivo',
      TipoCalificacion.POBLACION_OBJETIVO,
      'poblacionObjetivo'
    ),

    CalificacionFieldsHelper.crearFieldBase(
      'Justificación',
      'Justificación',
      TipoCalificacion.JUSTIFICACION,
      'justificacion'
    ),

    CalificacionFieldsHelper.crearFieldBase(
      'Presupuesto',
      'Presupuesto',
      TipoCalificacion.PRESUPUESTO,
      'presupuesto'
    ),

    CalificacionFieldsHelper.crearFieldBase(
      'Resultados Esperados',
      'Resultados esperados',
      TipoCalificacion.RESULTADOS_ESPERADOS,
      'resultadosEsperados'
    )
  ];

  fieldsArbolDeProblemas: FieldCalificacion[] = [

    CalificacionFieldsHelper.crearFieldBase(
      'Descripción',
      'Árbol de problemas',
      TipoCalificacion.ARBOL_PROBLEMAS
    )
  ];

  fieldsCausasDelArbolDeProblemas: FieldCalificacion[] = [];
  fieldsEfectosDelArbolDeProblemas: FieldCalificacion[] = [];
  
  fieldsArbolDeObjetivos: FieldCalificacion[] = [

    CalificacionFieldsHelper.crearFieldBase(
      'Descripción',
      'Árbol de objetivos',
      TipoCalificacion.ARBOL_OBJETIVOS
    )
  ];

  fieldsMediosDelArbolDeObjetivos: FieldCalificacion[] = [];
  fieldsFinesDelArbolDeObjetivos: FieldCalificacion[] = [];

  constructor(
    private calificacionService: CalificacionService,
    private usuarioService: UsuarioService,
    private proyectoService: ProyectoService,
    private convocatoriaService: ConvocatoriaService,
    private arbolDeProblemasService: ArbolDeProblemasService,
    private arbolDeObjetivosService: ArbolDeObjetivosService,
    private causaService: CausaService,
    private efectoService: EfectoService,
    private medioService: MedioService,
    private finService: FinService,
    private authService: AuthService,
    private router: Router,
    private datePipe: DatePipe
  ) {}

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.authService.currentUser.subscribe(user => {

      this.currentUser = user;

      if (!this.currentUser?.username) {
        return;
      }

      this.usuarioService
        .getUsuarioPorEmail(
          this.currentUser.username
        )
        .subscribe(usuario => {

          this.usuarioEvaluador = usuario;

          this.cargarCalificacionesPorIdUsuarioEvaluador();

        });
    });
  }

  // =====================================================
  // SECCIONES
  // =====================================================

  private construirSecciones(): void {

    this.secciones = [

      {
        titulo: this.tituloCalificacionFormulacionDelProyecto,
        fields: this.fieldsFormulacionDelProyecto,
        prefix: 'fp'
      },

      {
        titulo: this.tituloCalificacionArbolDeProblemas,
        fields: this.fieldsArbolDeProblemas,
        prefix: 'ap'
      },

      {
        titulo: this.tituloCalificacionCausasDelArbolDeProblemas,
        fields: this.fieldsCausasDelArbolDeProblemas,
        prefix: 'ca'
      },

      {
        titulo: this.tituloCalificacionEfectosDelArbolDeProblemas,
        fields: this.fieldsEfectosDelArbolDeProblemas,
        prefix: 'ef'
      },

      {
        titulo: this.tituloCalificacionArbolDeObjetivos,
        fields: this.fieldsArbolDeObjetivos,
        prefix: 'ao'
      },

      {
        titulo: this.tituloCalificacionMediosDelArbolDeObjetivos,
        fields: this.fieldsMediosDelArbolDeObjetivos,
        prefix: 'me'
      },

      {
        titulo: this.tituloCalificacionFinesDelArbolDeObjetivos,
        fields: this.fieldsFinesDelArbolDeObjetivos,
        prefix: 'fi'
      }
    ];
  }

  // =====================================================
  // CALIFICACIONES
  // =====================================================

  cargarCalificacionesPorIdUsuarioEvaluador(): void {

    this.calificacionService
      .getCalificacionesPorIdUsuarioEvaluador(
        this.usuarioEvaluador.id
      )
      .subscribe({
         next: (calificaciones) => {
          this.calificaciones =
            Array.isArray(calificaciones)
              ? calificaciones
              : [];

            this.cargarNombresDeProyectosUsuariosConvocatorias();

          },
          error: (err) => {
            console.error(err);
                    swal.fire({
                      icon: 'error',
                      title: 'Error',
                      text:
                        err.error?.mensajes?.[0]
                        || 'Error al consultar calificaciones'
              });
          }
      });
  }

  cargarNombresDeProyectosUsuariosConvocatorias(): void {

    const observables =
      this.calificaciones.map(calificacion => {

        return forkJoin({

          proyecto:
            this.proyectoService.getProyecto(
              calificacion.proyectoId
            ),

          investigador:
            this.usuarioService.getUsuarioPorId(
              calificacion.investigadorId
            ),

          tutor:
            calificacion.tutorId
            ? this.usuarioService.getUsuarioPorId(calificacion.tutorId)

            : calificacion.idTutorHistorico
              ? of(null)

            : this.usuarioService
                .getUsuarioTutorPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioEvaluador(
                  calificacion.proyectoId,
                  calificacion.investigadorId,
                  this.usuarioEvaluador.id
                ),

          convocatoria:
            this.convocatoriaService
              .getConvocatoriaPorIdProyectoYIdUsuarioInvestigador(
                calificacion.proyectoId,
                calificacion.investigadorId
              ),

          evaluador:
            of(this.usuarioEvaluador)
        });
      });

    forkJoin(observables)
      .subscribe(resultados => {

        this.calificacionesNomPIT =
          resultados.map((res, index) => {

            const calificacion =
              this.calificaciones[index];

            const item =
              new CalificacionNomPIT();

            item.calificacion =
              calificacion;

            item.proyecto =
              res.proyecto;

            item.nombresapellidosUsuarioInvestigador =
              `${res.investigador.nombre}
              ${res.investigador.apellidos}`;

            item.nombresapellidosUsuarioTutor =
            res.tutor
              ? `${res.tutor.nombre} ${res.tutor.apellidos}`
              : calificacion.nombreTutorHistorico;

            item.nombresapellidosUsuarioEvaluador =
              `${res.evaluador.nombre}
              ${res.evaluador.apellidos}`;

            item.tituloConvocatoria =
              res.convocatoria.titulo;

            item.fechaCalificacionFormateada =
              calificacion.fechaCreacion
                ? this.datePipe.transform(
                    calificacion.fechaCreacion,
                    'dd/MM/yyyy HH:mm:ss'
                  ) || ''
                : '';

            return item;
          });
      });
  }

  // =====================================================
  // DETALLE
  // =====================================================

  async mostrarDetalleCalificacion(
    idCalificacion: number
  ): Promise<void> {

    try {

      this.visibleDetalleCalificacion = true;

      this.construirSecciones();

      this.calificacionSeleccionada =
        await firstValueFrom(
          this.calificacionService
            .getCalificacionPorId(
              idCalificacion
            )
        );

      this.proyectoSeleccionado =
        await firstValueFrom(
          this.proyectoService
            .getProyecto(
              this.calificacionSeleccionada.proyectoId
            )
        );

      this.cargarfieldsFormulacionDelProyecto();

      await this.cargarTodoRelacionado();

    } catch (error) {

      console.error(error);

      swal.fire(
        'Error',
        'Error al cargar detalle',
        'error'
      );
    }
  }

  // =====================================================
  // RELACIONADOS
  // =====================================================

  async cargarTodoRelacionado(): Promise<void> {

    try {

      const {
        arbolProblemas,
        arbolObjetivos
      } = await firstValueFrom(

        forkJoin({

          arbolProblemas:
            this.arbolDeProblemasService
              .getArbolDeProblemasPorIdProyecto(
                this.proyectoSeleccionado.id
              ),

          arbolObjetivos:
            this.arbolDeObjetivosService
              .getArbolDeObjetivosPorIdProyecto(
                this.proyectoSeleccionado.id
              )
        })
      );

      this.arbolDeProblemas =
        arbolProblemas;

      this.arbolDeObjetivos =
        arbolObjetivos;

      this.cargarfieldsArbolDeProblemas();

      this.cargarfieldsArbolDeObjetivos();

      const observables: any = {};

      if (this.arbolDeProblemas?.id) {

        observables.causas =
          this.causaService
            .getCausasPorIdArbolDeProblemas(
              this.arbolDeProblemas.id
            );

        observables.efectos =
          this.efectoService
            .getEfectosPorIdArbolDeProblemas(
              this.arbolDeProblemas.id
            );
      }

      if (this.arbolDeObjetivos?.id) {

        observables.medios =
          this.medioService
            .getMediosPorIdArbolDeObjetivos(
              this.arbolDeObjetivos.id
            );

        observables.fines =
          this.finService
            .getFinesPorIdArbolDeObjetivos(
              this.arbolDeObjetivos.id
            );
      }

      const resultados: any =
        await firstValueFrom(
          forkJoin(observables)
        );

      this.fieldsCausasDelArbolDeProblemas =
        CalificacionFieldsHelper.crearFieldsDinamicos(
          resultados.causas,
          'Causa',
          TipoCalificacion.CAUSA
        );

      this.fieldsEfectosDelArbolDeProblemas =
        CalificacionFieldsHelper.crearFieldsDinamicos(
          resultados.efectos,
          'Efecto',
          TipoCalificacion.EFECTO
        );

      this.fieldsMediosDelArbolDeObjetivos =
        CalificacionFieldsHelper.crearFieldsDinamicos(
          resultados.medios,
          'Medio',
          TipoCalificacion.MEDIO
        );

      this.fieldsFinesDelArbolDeObjetivos =
        CalificacionFieldsHelper.crearFieldsDinamicos(
          resultados.fines,
          'Fin',
          TipoCalificacion.FIN
        );

      this.construirSecciones();

      this.mapearCalificacionAFormulario();

    } catch (error) {

      console.error(error);

      swal.fire(
        'Error',
        'Error cargando información relacionada',
        'error'
      );
    }
  }

  // =====================================================
  // MAPEAR
  // =====================================================

  mapearCalificacionAFormulario(): void {

    if (!this.calificacionSeleccionada?.id) {
      return;
    }

    this.obtenerTodosLosFields()
      .forEach(field => {

        CalificacionMapperHelper
          .aplicarEvaluacionAField(

            field,
            this.calificacionSeleccionada,
            RolCalificador.EVALUADOR,
            this.usuarioEvaluador.id
          );
      });
  }

  // =====================================================
  // CARGAR FIELDS
  // =====================================================

  cargarfieldsFormulacionDelProyecto(): void {

    this.fieldsFormulacionDelProyecto
      .forEach(field => {

        field.value =
          this.proyectoSeleccionado[
            field.propiedadProyecto
          ] ?? '';
      });
  }

  cargarfieldsArbolDeProblemas(): void {

    this.fieldsArbolDeProblemas
      .forEach(field => {

        field.value =
          this.arbolDeProblemas.descripcion;

        field.referenciaId =
          this.arbolDeProblemas.id;
      });
  }

  cargarfieldsArbolDeObjetivos(): void {

    this.fieldsArbolDeObjetivos
      .forEach(field => {

        field.value =
          this.arbolDeObjetivos.descripcion;

        field.referenciaId =
          this.arbolDeObjetivos.id;
      });
  }

  // =====================================================
  // HELPERS
  // =====================================================

  private obtenerTodosLosFields(): FieldCalificacion[] {

    return [

      ...this.fieldsFormulacionDelProyecto,
      ...this.fieldsArbolDeProblemas,
      ...this.fieldsCausasDelArbolDeProblemas,
      ...this.fieldsEfectosDelArbolDeProblemas,
      ...this.fieldsArbolDeObjetivos,
      ...this.fieldsMediosDelArbolDeObjetivos,
      ...this.fieldsFinesDelArbolDeObjetivos

    ];
  }

  getMin(
    estado?: EstadoCalificacion | null
  ): number {

    return CalificacionUtilsHelper
      .getMin(estado);
  }

  getMax(
    estado?: EstadoCalificacion | null
  ): number {

    return CalificacionUtilsHelper
      .getMax(estado);
  }

  getScoreValidationClass(
    field: FieldCalificacion
  ): string {

    return CalificacionUtilsHelper
      .getScoreValidationClass(
        field.puntaje,
        field.calificacion
      );
  }

  ajustarCalificacion(
    calificacion:
    number | undefined | null
  ): number {
  
    return CalificacionUtilsHelper.ajustarCalificacion(calificacion);

  }

  getEstadoDesdePuntaje(
    puntaje: number | null
  ): EstadoCalificacion {
    return CalificacionUtilsHelper.getEstadoDesdePuntaje(puntaje);

  }

  // =====================================================
  // ELIMINAR
  // =====================================================

  eliminarCalificacion(calificacion: Calificacion): void {

    swal.fire({
      title: 'Está seguro?',
      text:
        `¿Seguro que desea eliminar la calificación: ${calificacion.id}?`,
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

    } as any).then(result => {

      if (!result.value) {

        swal.fire(
          'Cancelado',
          'Eliminación cancelada',
          'info'
        );

        return;
      }

      this.calificacionService.deleteEvaluacionesEvaluador(calificacion.id)
        .subscribe({
          next:() => {
            this.calificacionesNomPIT =
              this.calificacionesNomPIT
                .filter(item =>

                  item.calificacion.id !==
                  calificacion.id
                );

                console.log('Calificacion eliminada con exito')
            swal.fire(
              'Eliminada',
              'Calificación eliminada con éxito',
              'success'
            );
           },
            error: (err) => {
              console.error(err);
              swal.fire({
                icon: 'error',
                title: 'Error',
                text:
                  err.error?.mensajes?.[0]
                  || 'Error al consultar calificaciones'
              });
            }

        });
    });
  }

  // =====================================================
  // LOGOUT
  // =====================================================

  logout(): void {

    this.authService.logout();

    this.router.navigate([
      '/login'
    ]);
  }
}