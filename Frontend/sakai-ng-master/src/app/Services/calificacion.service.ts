import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Calificacion } from '../Models/calificacion.model';

@Injectable({
  providedIn: 'root'
})
export class CalificacionService {
  private apiUrl = `${environment.apiUrl}/calificaciones`;

  constructor( private http: HttpClient) {}

createCalificacion(calificacion: Calificacion) : Observable<Calificacion> {
       //return this.http.post<Calificacion>(this.apiUrl, calificacion,{headers: this.httpHeaders})
         return this.http.post<Calificacion>(this.apiUrl, calificacion)
                    .pipe(map((response) => response as Calificacion),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear la calificacion", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );

  }

   getCalificaciones(): Observable <Calificacion[]>
    {
      return this.http.get<Calificacion[]>(this.apiUrl)
                      .pipe(map((response) => response as Calificacion[]),
                       catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );
    }

  getCalificacionPorId(IdCalificacion: number): Observable<Calificacion>{

    return this.http.get<Calificacion>(`${this.apiUrl}/${IdCalificacion}`)
    .pipe(
        catchError((e) => {
          //console.error(e.error.mensaje);
          return throwError(() => e);
        })
    );
  }

   getCalificacionesPorIdProyecto(idProyecto: number): Observable <Calificacion[]>
    {
      return this.http.get<Calificacion[]>(`${this.apiUrl}/proyecto/${idProyecto}`)
                      .pipe(map((response) => response as Calificacion[]),
                       catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );
    }

    getCalificacionesPorIdUsuarioInvestigador(idUsuarioInvestigador: number): Observable <Calificacion[]>
    {
      return this.http.get<Calificacion[]>(`${this.apiUrl}/usuarioinvestigador/${idUsuarioInvestigador}`)
                      .pipe(map((response) => response as Calificacion[]),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
            );
    }

    getCalificacionesPorIdUsuarioTutor(idUsuarioTutor: number): Observable <Calificacion[]>
     {
       return this.http.get<Calificacion[]>(`${this.apiUrl}/usuariotutor/${idUsuarioTutor}`)
                       .pipe(map((response) => response as Calificacion[]),
                        catchError((e) => {
                           //console.error(e.error.mensaje);
                           //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                           return throwError(() => e);
                       })
              );
     }

    getCalificacionesPorIdUsuarioEvaluador(idUsuarioEvaluador: number): Observable <Calificacion[]>
     {
       return this.http.get<Calificacion[]>(`${this.apiUrl}/usuarioevaluador/${idUsuarioEvaluador}`)
                       .pipe(map((response) => response as Calificacion[]),
                        catchError((e) => {
                           //console.error(e.error.mensaje);
                           //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                           return throwError(() => e);
                       })
              );
     }


    getCalificacionesPorIdUsuarioInvestigadorYIdUsuarioTutorNoNulo(idUsuarioInvestigador: number): Observable <Calificacion[]>
    {
      return this.http.get<Calificacion[]>(`${this.apiUrl}/usuarioinvestigadoryusuariotutornonulo/${idUsuarioInvestigador}`)
                      .pipe(map((response) => response as Calificacion[]),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("error al consultar calificaciones en la bd", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
            );
    }
      

  updateCalificacion(calificacion: Calificacion): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${calificacion.id}`,
                     calificacion)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}
  

deleteCalificacion(idCalificacion: number): Observable<Calificacion>{

    return this.http.delete<Calificacion>(`${this.apiUrl}/${idCalificacion}`)
    .pipe(
    catchError(e => {
        console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
    })
    );
}

deleteEvaluacionesTutor(idCalificacion: number): Observable<any> {
  return this.http.delete<any>(`${this.apiUrl}/${idCalificacion}/tutor`)
    .pipe(
      catchError(e => {
        console.error(e.error?.mensaje || e.message);
        return throwError(() => e);
      })
    );
}

deleteEvaluacionesEvaluador(idCalificacion: number): Observable<any> {
  return this.http.delete<any>(`${this.apiUrl}/${idCalificacion}/evaluador`)
    .pipe(
      catchError(e => {
        console.error(e.error?.mensaje || e.message);
        return throwError(() => e);
      })
    );
}

  

  /*
  getConvocatorias(): Observable <Convocatoria[]>
  {
    return this.http.get<Convocatoria[]>(this.apiUrl)
                    .pipe(map((response) => response as Convocatoria[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

  updateConvocatoria(convocatoria: Convocatoria): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${convocatoria.id}`,
                     convocatoria)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}

  deleteConvocatoria(idConvocatoria: number): Observable<Convocatoria>{

    return this.http.delete<Convocatoria>(`${this.apiUrl}/${idConvocatoria}`)
    .pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );

  }

*/

}
