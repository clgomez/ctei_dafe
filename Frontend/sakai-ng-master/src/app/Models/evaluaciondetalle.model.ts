import { RolCalificador } from "@app/Enums/Calificaciones/rolcalificador.enum";

 export class EvaluacionDetalle {

    id?: number;
    calificacionId?: number;
    rubroId?: number;
    usuarioId: number;
    nombreUsuario: string;
    idUsuarioHistorico?: number;
    nombreUsuarioHistorico?: string;
    rolCalificador: RolCalificador;
    nota?: number | null;
    comentario: string;
    activo: boolean;
 
  }