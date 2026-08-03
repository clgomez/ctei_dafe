import { Actividad } from "./actividad.model";

export class Cronograma {
    id?: number;
    fechaInicio?: Date | string;   // 👈 acepta ambos
    fechaFin?: Date | string;
    actividadId: number;

  }
