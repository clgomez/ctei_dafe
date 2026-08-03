import { EstadoCalificacion } from '@app/Enums/Calificaciones/estadocalificacion.enum';

export class CalificacionUtilsHelper {

  static getEstadoDesdePuntaje(
    puntaje: number | null
  ): EstadoCalificacion {

    if (puntaje === null || puntaje === undefined) {
      return EstadoCalificacion.PENDIENTE;
    }

    if (puntaje >= 0 && puntaje <= 33.3) {
      return EstadoCalificacion.NO_APROBADO;
    }

    if (puntaje >= 33.4 && puntaje <= 66.6) {
      return EstadoCalificacion.POR_MEJORAR;
    }

    return EstadoCalificacion.APROBADO;
  }

  static getMin(
    estado?: EstadoCalificacion | null
  ): number {

    if (!estado) return 0;

    switch (estado) {

      case EstadoCalificacion.NO_APROBADO:
        return 0.0;

      case EstadoCalificacion.POR_MEJORAR:
        return 33.4;

      case EstadoCalificacion.APROBADO:
        return 66.7;

      default:
        return 0;
    }
  }

  static getMax(
    estado?: EstadoCalificacion | null
  ): number {

    if (!estado) return 100;

    switch (estado) {

      case EstadoCalificacion.NO_APROBADO:
        return 33.3;

      case EstadoCalificacion.POR_MEJORAR:
        return 66.6;

      case EstadoCalificacion.APROBADO:
        return 100;

      default:
        return 100;
    }
  }

  static getScoreValidationClass(
    puntaje: number,
    estado: EstadoCalificacion
  ): string {

    if (!estado) {
      return '';
    }

    if (estado === EstadoCalificacion.PENDIENTE) {
      return 'score-pending';
    }

    const min = this.getMin(estado);
    const max = this.getMax(estado);

    if (puntaje >= min && puntaje <= max) {
      return 'score-valid';
    }

    return 'score-invalid';
  }

  static ajustarCalificacion(
    calificacion:
    number | undefined | null
  ): number {

    if (
      calificacion === undefined ||
      calificacion === null
    ) {
      return 0;
    }

    return Number(
      calificacion.toFixed(1)
    );
  }
}