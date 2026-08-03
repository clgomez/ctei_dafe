import { FieldCalificacion } from "./fieldcalificacion.interface";

export interface SeccionCalificacion {
  titulo: string;
  fields: FieldCalificacion[];
  prefix: string;
}