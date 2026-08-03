import { EstadoCalificacion } from "@app/Enums/Calificaciones/estadocalificacion.enum";
import { TipoCalificacion } from "@app/Enums/Calificaciones/tipocalificacion.enum";

export interface FieldCalificacion {
  label: string;
  descripcion: string;
  tipoCalificacion: TipoCalificacion;
  referenciaId?: number | null;
  propiedadProyecto?: string;
  value: string;
  calificacion: EstadoCalificacion | null;
  puntaje: number | null;
  disabled: boolean;
  comentarios: string;
}