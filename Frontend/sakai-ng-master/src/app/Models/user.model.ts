export interface User {
  id?: number;
  nombre: string;
  apellidos: string;
  username: string;
  email: string;
  password: string;
  direccion: string;
  estado: string;
  fecha_nacimiento: string;
  tipoIdentificacion: string;
  identificacion: string;
  genero: string;
  ocupacion: string;
  telefono: string;
  roles?: string[];
}
