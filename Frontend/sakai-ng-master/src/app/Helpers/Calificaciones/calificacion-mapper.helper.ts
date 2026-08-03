import { FieldCalificacion } from '@app/Interfaces/Calificaciones/fieldcalificacion.interface';
import { Calificacion } from '@app/Models/calificacion.model';
import { RubroCalificacion } from '@app/Models/rubrocalificacion.model';
import { EvaluacionDetalle } from '@app/Models/evaluaciondetalle.model';
import { RolCalificador } from '@app/Enums/Calificaciones/rolcalificador.enum';
import { CalificacionUtilsHelper } from './calificacion-utils.helper';
import { EstadoCalificacion } from '@app/Enums/Calificaciones/estadocalificacion.enum';

export class CalificacionMapperHelper {

  static buscarRubro(
    calificacion: Calificacion,
    tipo: any,
    referenciaId?: number | null
  ): RubroCalificacion | undefined {

    return calificacion.rubros?.find(r =>

      String(r.tipoCalificacion) === String(tipo) &&
      Number(r.referenciaId ?? 0) === Number(referenciaId ?? 0)
    );
  }

  static buscarEvaluacionPorRolYUsuario(
    rubro: RubroCalificacion,
    rol: RolCalificador,
    usuarioId: number
  ): EvaluacionDetalle | undefined {

    return rubro.evaluaciones?.find(e =>

      String(e.rolCalificador) === String(rol) &&
      (
        Number(e.usuarioId) === Number(usuarioId)
        ||
        Number(e.idUsuarioHistorico) === Number(usuarioId)
      )
    );
}  

// NUEVO: limpiar field
  static limpiarField(
    field: FieldCalificacion
  ): void {

    field.puntaje = 0;
    field.comentarios = '';
    field.calificacion = EstadoCalificacion.PENDIENTE;
    field.disabled = true;
  }

  static aplicarEvaluacionAField(
    field: FieldCalificacion,
    calificacion: Calificacion,
    rol: RolCalificador,
    usuarioId: number
  ): void {

    // Siempre limpiar primero
    this.limpiarField(field);

    const rubro = this.buscarRubro(
      calificacion,
      field.tipoCalificacion,
      field.referenciaId
    );

    if (!rubro) return;

    const evaluacion =
      this.buscarEvaluacionPorRolYUsuario(
        rubro,
        rol,
        usuarioId
      );

    if (!evaluacion) return;

    field.puntaje =
      Number(Number(evaluacion.nota).toFixed(1));

    field.comentarios =
      evaluacion.comentario ?? '';

    field.calificacion =
      CalificacionUtilsHelper.getEstadoDesdePuntaje(
        field.puntaje
      );

    field.disabled = false;
      
  }
}