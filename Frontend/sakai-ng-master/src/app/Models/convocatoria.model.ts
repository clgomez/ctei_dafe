/*export class Convocatoria {
    id?: number;
    descripcion: string;
    fechaInicio: string;
    fechaFin: string;
    estado: string;

  }*/

export class Convocatoria {
  id?: number;
  titulo?: string;
  descripcion?: string;
  fechaInicio?: Date | string;   // 👈 acepta ambos
  fechaFin?: Date | string;
  estado?: string;
}

