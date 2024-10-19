import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { InscripcionProyectoDTO } from '../Models/inscripcionproyecto-dto.model';
import { Inscripcion } from '../Models/inscripcion.model';

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

  getInscripciones(): Observable <Inscripcion[]>
  {
    return this.http.get<Inscripcion[]>(this.apiUrl)
                    .pipe(map((response) => response as Inscripcion[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }


  updateInscripcion(IdInscripcion: number, inscripcionProyectoDTO: InscripcionProyectoDTO): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${IdInscripcion}`,inscripcionProyectoDTO)
                    .pipe(
                      catchError((e) => {
                        //console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}

  getInscripcion(IdInscripcion: number): Observable<InscripcionProyectoDTO>{

    return this.http.get<InscripcionProyectoDTO>(`${this.apiUrl}/${IdInscripcion}`)
    .pipe(
        catchError((e) => {
          //console.error(e.error.mensaje);
          return throwError(() => e);
        })
    );
  }


  /*
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
