import { TipoCalificacion } from '@app/Enums/Calificaciones/tipocalificacion.enum';
import { FieldCalificacion } from '@app/Interfaces/Calificaciones/fieldcalificacion.interface';

export class CalificacionFieldsHelper {

  static crearFieldBase(
    label: string,
    descripcion: string,
    tipo: TipoCalificacion,
    propiedadProyecto?: string
  ): FieldCalificacion {

    return {
      label,
      descripcion,
      tipoCalificacion: tipo,
      propiedadProyecto,
      referenciaId: null,
      value: '',
      calificacion: null,
      puntaje: 0.0,
      disabled: true,
      comentarios: ''
    };
  }

  static crearFieldsDinamicos(
    items: any[],
    label: string,
    tipo: TipoCalificacion
  ): FieldCalificacion[] {

    return (items || []).map(item => ({

      label,
      descripcion: item.descripcion,
      tipoCalificacion: tipo,
      referenciaId: item.id,
      value: item.descripcion,
      calificacion: null,
      puntaje: 0.0,
      disabled: true,
      comentarios: ''

    }));
  }
}