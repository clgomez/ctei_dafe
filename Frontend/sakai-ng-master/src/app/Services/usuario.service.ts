import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { User } from '../Models/user.model';
import { Usuario } from '../Models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuario`;

  constructor( private http: HttpClient) {}


  getUsuariosPorRol(nombreRol: string): Observable <User[]>
  {
    return this.http.get<User[]>(`${this.apiUrl}/rol/${nombreRol}`)
                    .pipe(map((response) => response as User[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar usuarios en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

  getUsuarioPorId(idUsuario: number): Observable<Usuario>{

    return this.http.get<Usuario>(`${this.apiUrl}/${idUsuario}`)
  }

  getUsuario(idUsuario: number): Observable<User>{

    return this.http.get<User>(`${this.apiUrl}/${idUsuario}`)
  }


  getUsuarioPorIdentificacion(identificacionUsuario: string): Observable<Usuario>{

    return this.http.get<Usuario>(`${this.apiUrl}/identificacion/${identificacionUsuario}`)
  }

  getUsuarios(): Observable <Usuario[]>
  {
    return this.http.get<Usuario[]>(this.apiUrl)
                    .pipe(map((response) => response as Usuario[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar usuarios en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

  updateUsuario(usuario: Usuario): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${usuario.id}`,usuario)
                    .pipe(
                      catchError((e) => {
                        //console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
  }

 
  deleteUsuario(idUsuario: number): Observable<Usuario>{

    return this.http.delete<Usuario>(`${this.apiUrl}/${idUsuario}`)
    .pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );

  }

  getUsuarioPorEmail(email: string): Observable<Usuario>{

    return this.http.get<Usuario>(`${this.apiUrl}/emailusuario/${email}`)
  }

  getUsuarioTutorPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioEvaluador(IdProyecto: number, IdUsuarioInvestigador: number, IdUsuarioEvaluador: number): Observable<Usuario>{

    return this.http.get<Usuario>(`${this.apiUrl}/proyecto/${IdProyecto}/usuarioinvestigador/${IdUsuarioInvestigador}/usuarioevaluador/${IdUsuarioEvaluador}`)
    .pipe(
        catchError((e) => {
          //console.error(e.error.mensaje);
          return throwError(() => e);
        })
    );
  }

  getUsuarioEvaluadorPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioTutor(IdProyecto: number, IdUsuarioInvestigador: number, IdUsuarioTutor: number): Observable<Usuario>{

    return this.http.get<Usuario>(`${this.apiUrl}/proyecto/${IdProyecto}/usuarioinvestigador/${IdUsuarioInvestigador}/usuariotutor/${IdUsuarioTutor}`)
    .pipe(
        catchError((e) => {
          //console.error(e.error.mensaje);
          return throwError(() => e);
        })
    );
  }



}
