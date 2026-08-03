import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { ArbolDeProblemas } from '../Models/arboldeproblemas.model';

@Injectable({
  providedIn: 'root'
})
export class ArbolDeProblemasService {
  private apiUrl = `${environment.apiUrl}/arboles-problemas`;

  constructor( private http: HttpClient) {}

createArbolDeProblemas(arbolDeProblemas: ArbolDeProblemas) : Observable<any> {
       //return this.http.post<ArbolDeProblemas>(this.apiUrl, arbolDeProblemas,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, arbolDeProblemas)
                    .pipe(map((response) => response as ArbolDeProblemas),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

  getArbolDeProblemas(IdArbolDeProblemas: number): Observable<ArbolDeProblemas>{

    return this.http.get<ArbolDeProblemas>(`${this.apiUrl}/${IdArbolDeProblemas}`)
    .pipe(
        catchError((e) => {
          return throwError(() => e);
        })
    );
  }

 
   getArbolDeProblemasPorIdProyecto(idProyecto: number): Observable<ArbolDeProblemas | null> {
    return this.http.get<ArbolDeProblemas>(`${this.apiUrl}/proyecto/${idProyecto}`).pipe(
      catchError((e) => {
        return throwError(() => e);
      })
    );
  }  

  updateArbolDeProblemas(arbolDeProblemas: ArbolDeProblemas): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${arbolDeProblemas.id}`,
                     arbolDeProblemas)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
}
  

   deleteArbolDeProblemas(idArbolDeProblemas: number): Observable<any>{
  
      return this.http.delete<any>(`${this.apiUrl}/${idArbolDeProblemas}`)
      .pipe(
        catchError(e => {
          return throwError(() => e);
        })
      );
  
    }

}
