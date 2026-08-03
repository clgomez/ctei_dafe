export interface User {
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
  roles?: string[];

}
