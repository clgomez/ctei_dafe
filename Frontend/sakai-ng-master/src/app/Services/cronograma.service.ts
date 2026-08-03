import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Cronograma } from '../Models/cronograma.model';

@Injectable({
  providedIn: 'root'
})
export class CronogramaService {
  private apiUrl = `${environment.apiUrl}/cronogramas`;

  constructor( private http: HttpClient) {}

createCronograma(cronograma: Cronograma) : Observable<any> {
       //return this.http.post<Cronograma>(this.apiUrl, cronograma,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, cronograma)
                    .pipe(map((response) => response as Cronograma),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

  getCronogramaPorIdActividad(idActividad: number): Observable<Cronograma | null> {
    return this.http.get<Cronograma>(`${this.apiUrl}/actividad/${idActividad}`)
    .pipe(map((response) => response as Cronograma),
                catchError((e) => {
                return throwError(() => e);
        })

      );

  }

  updateCronograma(cronograma: Cronograma): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${cronograma.id}`,cronograma)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}


}
