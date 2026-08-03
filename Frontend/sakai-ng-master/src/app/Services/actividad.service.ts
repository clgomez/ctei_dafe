import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Actividad } from '../Models/actividad.model';

@Injectable({
  providedIn: 'root'
})
export class ActividadService {
  private apiUrl = `${environment.apiUrl}/actividades`;

  constructor( private http: HttpClient) {}

createActividad(actividad: Actividad) : Observable<any> {
       //return this.http.post<Actividad>(this.apiUrl, actividad,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, actividad)
                    .pipe(map((response) => response as Actividad),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

 getActividadesPorIdProyecto(idProyecto: number): Observable <Actividad[]>
  {
    return this.http.get<Actividad[]>(`${this.apiUrl}/proyecto/${idProyecto}`)
                    .pipe(map((response) => response as Actividad[]),
                     catchError((e) => {
                        return throwError(() => e);
                    })
           );
  }


  updateActividad(actividad: Actividad): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${actividad.id}`,actividad)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
}


  deleteActividad(idActividad: number): Observable<any>{
    
        return this.http.delete<any>(`${this.apiUrl}/${idActividad}`)
        .pipe(
          catchError(e => {
            return throwError(() => e);
          })
        );
    
  }

}
