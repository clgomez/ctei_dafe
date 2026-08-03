import { Rol } from "./rol.model";

export class Usuario {
    id?: number;
    nombre: string;
    apellidos: string;
    username: string;
    email: string;
    password: string;
    direccion: string;
    estado: string;
    fechaNacimiento: string;
    tipoIdentificacion: string;
    identificacion: string;
    genero: string;
    ocupacion: string;
    telefono: string;
    fechaRegistro: string;
    roles?: Rol[];

  }
