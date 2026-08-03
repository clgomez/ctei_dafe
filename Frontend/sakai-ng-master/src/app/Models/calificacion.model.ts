import { RubroCalificacion } from './rubrocalificacion.model';


export class Calificacion {

    id?: number;
    calificacionFinal: number = 0;
    fechaCreacion: string;
    estado: string;
    semaforo: string;
    notaTutor: number;
    notaEvaluador: number;
    idTutorHistorico: number;
    idEvaluadorHistorico: number;
    nombreTutorHistorico: string;
    nombreEvaluadorHistorico: string;
    proyectoId: number;
    investigadorId: number;
    tutorId: number;
    evaluadorId: number;
    rubros?: RubroCalificacion[] = [];

  }