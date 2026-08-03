import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Medio } from '../Models/medio.model';

@Injectable({
  providedIn: 'root'
})
export class MedioService {
  private apiUrl = `${environment.apiUrl}/medios`;

  constructor( private http: HttpClient) {}

createMedio(medio: Medio) : Observable<any> {
       //return this.http.post<Medio>(this.apiUrl, medio,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, medio)
                    .pipe(map((response) => response as Medio),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }


    getMediosPorIdArbolDeObjetivos(idArbolDeObjetivos: number): Observable<Medio[] | null> {
    return this.http.get<Medio[]>(`${this.apiUrl}/arbol/${idArbolDeObjetivos}`).pipe(
      catchError((e) => {
        return throwError(() => e);
      })
    );
  }

  getMedio(idMedio: number): Observable<Medio>{

    return this.http.get<Medio>(`${this.apiUrl}/${idMedio}`)
  }

  updateMedio(medio: Medio): Observable<any>{
  
        return this.http.put<any>(`${this.apiUrl}/${medio.id}`,
                        medio)
                        .pipe(
                          catchError((e) => {
                            return throwError(() => e);
                          })
                  );
    }

    deleteMedio(idMedio: number): Observable<any>{
    
        return this.http.delete<any>(`${this.apiUrl}/${idMedio}`)
        .pipe(
          catchError(e => {
            return throwError(() => e);
          })
        );
    
      }

}
