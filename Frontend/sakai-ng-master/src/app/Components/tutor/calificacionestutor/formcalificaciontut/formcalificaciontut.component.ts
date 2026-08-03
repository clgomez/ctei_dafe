import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';
import { firstValueFrom, forkJoin } from 'rxjs';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Usuario } from '@app/Models/usuario.model';
import { UsuarioService } from '@app/Services/usuario.service';
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { ArbolDeObjetivos } from '@app/Models/arboldeobjetivos.model';
import { ArbolDeObjetivosService } from '@app/Services/arboldeobjetivos.service';
import { CausaService } from '@app/Services/causa.service';
import { EfectoService } from '@app/Services/efecto.service';
import { MedioService } from '@app/Services/medio.service';
import { FinService } from '@app/Services/fin.service';
import { Calificacion } from '@app/Models/calificacion.model';
import { CalificacionService } from '@app/Services/calificacion.service';
import { TipoCalificacion } from '@app/Enums/Calificaciones/tipocalificacion.enum';
import { EstadoCalificacion } from '@app/Enums/Calificaciones/estadocalificacion.enum';
import { FieldCalificacion } from '@app/Interfaces/Calificaciones/fieldcalificacion.interface';
import { CalificacionFieldsHelper } from '@app/Helpers/Calificaciones/calificacion-fields.helper'; 
import { CalificacionUtilsHelper } from '@app/Helpers/Calificaciones/calificacion-utils.helper'; 
import { CalificacionMapperHelper } from '@app/Helpers/Calificaciones/calificacion-mapper.helper'; 
import { CalificacionBuilderHelper } from '@app/Helpers/Calificaciones/calificacion-builder.helper'; 
import { RolCalificador } from '@app/Enums/Calificaciones/rolcalificador.enum';

@Component({
  selector: 'app-formcalificaciontut',
  templateUrl: './formcalificaciontut.component.html',
  styleUrls: ['./formcalificaciontut.component.css']
})
export class FormCalificacionTutComponent implements OnInit {

  public proyecto: Proyecto = new Proyecto();
  public usuarioTutor: Usuario = new Usuario();

  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();
  public arbolDeObjetivos: ArbolDeObjetivos = new ArbolDeObjetivos();

  public calificacion: Calificacion = new Calificacion();

  public EstadoCalificacion = EstadoCalificacion;

  public secciones: any[] = [];

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
    private proyectoService: ProyectoService,
    private usuarioService: UsuarioService,
    private arbolDeProblemasService: ArbolDeProblemasService,
    private arbolDeObjetivosService: ArbolDeObjetivosService,
    private causaService: CausaService,
    private efectoService: EfectoService,
    private medioService: MedioService,
    private finService: FinService,
    private calificacionService: CalificacionService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {

    try {

      this.construirSecciones();

      const params =
        await firstValueFrom(
          this.activatedRoute.params
        );

      const idCalificacion =
        Number(params['idcalificacion']);

      const idProyecto =
        Number(params['idproyecto']);

      // =================================================
      // EDITAR
      // =================================================

      if (idCalificacion) {

        this.calificacion =
          await firstValueFrom(
            this.calificacionService.getCalificacionPorId(
              idCalificacion
            )
          );

        this.usuarioTutor =
          await firstValueFrom(
            this.usuarioService.getUsuarioPorId(
              this.calificacion.tutorId
            )
          );

        await this.cargarProyecto(
          this.calificacion.proyectoId
        );

        return;
      }

      // =================================================
      // NUEVA
      // =================================================

      if (idProyecto) {

        const idusuariotutor =
          Number(params['idusuariotutor']);

        this.usuarioTutor =
          await firstValueFrom(
            this.usuarioService.getUsuarioPorId(
              idusuariotutor
            )
          );

        await this.cargarProyecto(idProyecto);
      }

    } catch (error) {

      console.error(error);

      swal.fire(
        'Error',
        'Error cargando componente',
        'error'
      );
    }
  }

  // =====================================================
  // SECCIONES
  // =====================================================

  private construirSecciones(): void {

    this.secciones = [
      {
        titulo: 'Calificar Formulación Del Proyecto',
        fields: this.fieldsFormulacionDelProyecto,
        prefix: 'fp'
      },
      {
        titulo: 'Calificar Árbol De Problemas',
        fields: this.fieldsArbolDeProblemas,
        prefix: 'ap'
      },
      {
        titulo: 'Calificar Causas Del Árbol De Problemas',
        fields: this.fieldsCausasDelArbolDeProblemas,
        prefix: 'ca'
      },
      {
        titulo: 'Calificar Efectos Del Árbol De Problemas',
        fields: this.fieldsEfectosDelArbolDeProblemas,
        prefix: 'ef'
      },
      {
        titulo: 'Calificar Árbol De Objetivos',
        fields: this.fieldsArbolDeObjetivos,
        prefix: 'ao'
      },
      {
        titulo: 'Calificar Medios Del Árbol De Objetivos',
        fields: this.fieldsMediosDelArbolDeObjetivos,
        prefix: 'me'
      },
      {
        titulo: 'Calificar Fines Del Árbol De Objetivos',
        fields: this.fieldsFinesDelArbolDeObjetivos,
        prefix: 'fi'
      }
    ];
  }

  // =====================================================
  // CARGA
  // =====================================================

  async cargarProyecto(idProyecto: number): Promise<void> {

    this.proyecto =
      await firstValueFrom(
        this.proyectoService.getProyecto(idProyecto)
      );

    this.cargarfieldsFormulacionDelProyecto();

    await this.cargarTodoRelacionado();
  }

  async cargarTodoRelacionado(): Promise<void> {

    const {
      arbolProblemas,
      arbolObjetivos
    } = await firstValueFrom(

      forkJoin({

        arbolProblemas:
          this.arbolDeProblemasService
            .getArbolDeProblemasPorIdProyecto(
              this.proyecto.id
            ),

        arbolObjetivos:
          this.arbolDeObjetivosService
            .getArbolDeObjetivosPorIdProyecto(
              this.proyecto.id
            )

      })
    );

    this.arbolDeProblemas = arbolProblemas;
    this.arbolDeObjetivos = arbolObjetivos;

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
  }

  // =====================================================
  // MAPEO
  // =====================================================

  mapearCalificacionAFormulario(): void {

    if (!this.calificacion?.id) {
      return;
    }

    this.obtenerTodosLosFields().forEach(field => {

      CalificacionMapperHelper.aplicarEvaluacionAField(
        field,
        this.calificacion,
        RolCalificador.TUTOR,
        this.usuarioTutor.id
      );

    });
  }

  // =====================================================
  // FIELDS
  // =====================================================

  cargarfieldsFormulacionDelProyecto(): void {

    this.fieldsFormulacionDelProyecto.forEach(field => {

      field.value =
        this.proyecto[field.propiedadProyecto];

    });
  }

  cargarfieldsArbolDeProblemas(): void {

    this.fieldsArbolDeProblemas.forEach(field => {

      field.value =
        this.arbolDeProblemas.descripcion;

      field.referenciaId =
        this.arbolDeProblemas.id;

    });
  }

  cargarfieldsArbolDeObjetivos(): void {

    this.fieldsArbolDeObjetivos.forEach(field => {

      field.value =
        this.arbolDeObjetivos.descripcion;

      field.referenciaId =
        this.arbolDeObjetivos.id;

    });
  }

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

  // =====================================================
  // HELPERS UI
  // =====================================================

  getMin(cal?: EstadoCalificacion | null): number {

    return CalificacionUtilsHelper.getMin(cal);
  }

  getMax(cal?: EstadoCalificacion | null): number {

    return CalificacionUtilsHelper.getMax(cal);
  }

  getTooltip(field: FieldCalificacion): string {

    if (!field.calificacion) {
      return 'Seleccione una calificación';
    }

    return `Rango permitido:
    ${this.getMin(field.calificacion)}
    -
    ${this.getMax(field.calificacion)}`;
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

  // =====================================================
  // EVENTOS
  // =====================================================

  onCalificacionChange(
    field: FieldCalificacion,
    esManual: boolean = false
  ) {

    field.disabled =
      field.calificacion ===
      EstadoCalificacion.PENDIENTE;

    if (!esManual) return;

    field.puntaje =
      CalificacionUtilsHelper.getMin(
        field.calificacion
      );
  }

  incrementar(field: FieldCalificacion) {

    const max =
      this.getMax(field.calificacion);

    if (field.puntaje < max) {

      field.puntaje =
        Number(
          (field.puntaje + 0.1).toFixed(1)
        );
    }
  }

  decrementar(field: FieldCalificacion) {

    const min =
      this.getMin(field.calificacion);

    if (field.puntaje > min) {

      field.puntaje =
        Number(
          (field.puntaje - 0.1).toFixed(1)
        );
    }
  }

  validarRango(field: FieldCalificacion) {

    const min =
      this.getMin(field.calificacion);

    const max =
      this.getMax(field.calificacion);

    if (field.puntaje < min) {
      field.puntaje = min;
    }

    if (field.puntaje > max) {
      field.puntaje = max;
    }
  }

  // =====================================================
  // GUARDAR
  // =====================================================

  private construirCalificacion(
    esActualizacion: boolean
  ): Calificacion {

    return CalificacionBuilderHelper
      .construirCalificacion(

        esActualizacion,

        this.proyecto,

        this.usuarioTutor,

        RolCalificador.TUTOR,

        this.calificacion,

        this.obtenerTodosLosFields()
      );
  }

  async CalificarProyecto(): Promise<void> {

    try {

      const calificacion =
        this.construirCalificacion(false);

      await firstValueFrom(
        this.calificacionService
          .createCalificacion(calificacion)
      );

      swal.fire(
        'Nueva Calificación',
        'Calificación creada con éxito',
        'success'
      );

      this.router.navigate([
        '/tutor/calificacionestutor/calificacionestut'
      ]);

    } catch (error: any) {

      console.error(error);

      swal.fire(
        'Error',
        error?.error?.mensaje ||
        'Error al crear la calificación',
        'error'
      );
    }
  }

  async RecalificarProyecto(): Promise<void> {

    try {

      const calificacion =
        this.construirCalificacion(true);

      await firstValueFrom(
        this.calificacionService
          .updateCalificacion(calificacion)
      );

      swal.fire(
        'Actualizada',
        'Recalificación realizada',
        'success'
      );

      this.router.navigate([
        '/tutor/calificacionestutor/calificacionestut'
      ]);

    } catch (error: any) {

      console.error(error);

      swal.fire(
        'Error',
        error?.error?.mensaje ||
        'Error al actualizar',
        'error'
      );
    }
  }

  cancelarCalificacion(): void {

    this.router.navigate([
      '/tutor/calificacionestutor/calificacionestut'
    ]);
  }
}