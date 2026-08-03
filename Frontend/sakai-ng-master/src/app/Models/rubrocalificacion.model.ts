import { TipoCalificacion } from "@app/Enums/Calificaciones/tipocalificacion.enum";
import { EvaluacionDetalle } from "./evaluaciondetalle.model";

export class RubroCalificacion {

    id?: number;
    tipoCalificacion: TipoCalificacion;
    referenciaId: number;
    nombre: string;
    evaluaciones?: EvaluacionDetalle[] = [];

  }
   
  