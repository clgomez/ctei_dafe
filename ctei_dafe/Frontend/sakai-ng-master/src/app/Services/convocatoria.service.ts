import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Convocatoria } from '../Models/convocatoria.model';

@Injectable({
  providedIn: 'root'
})
export class ConvocatoriaService {
  private apiUrl = `${environment.apiUrl}/convocatorias`;

  constructor( private http: HttpClient) {}

createConvocatoria(convocatoria: Convocatoria) : Observable<Convocatoria> {
       //return this.http.post<Convocatoria>(this.apiUrl, convocatoria,{headers: this.httpHeaders})
         return this.http.post<Convocatoria>(this.apiUrl, convocatoria)
                    .pipe(map((response) => response as Convocatoria),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear el producto", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );

  }

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

  getConvocatoria(idConvocatoria: number): Observable<Convocatoria>{

    return this.http.get<Convocatoria>(`${this.apiUrl}/${idConvocatoria}`)
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



}
