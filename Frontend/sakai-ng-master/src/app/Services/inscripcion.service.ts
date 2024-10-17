import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { InscripcionProyectoDTO } from '../Models/inscripcionproyecto-dto.model';

@Injectable({
  providedIn: 'root'
})
export class InscripcionService {
  private apiUrl = `${environment.apiUrl}/inscripciones`;

  constructor( private http: HttpClient) {}

createInscripcion(inscripcionProyectoDTO: InscripcionProyectoDTO) : Observable<any> {
       //return this.http.post<Convocatoria>(this.apiUrl, convocatoria,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, inscripcionProyectoDTO)
                    .pipe(map((response) => response as InscripcionProyectoDTO),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear el producto", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );

  }


  /*
  getInscripciones(): Observable <any>
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
