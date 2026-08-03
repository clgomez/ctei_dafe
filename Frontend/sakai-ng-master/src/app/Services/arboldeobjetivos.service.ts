import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { ArbolDeObjetivos } from '../Models/arboldeobjetivos.model';

@Injectable({
  providedIn: 'root'
})
export class ArbolDeObjetivosService {
  private apiUrl = `${environment.apiUrl}/arboles-objetivos`;

  constructor( private http: HttpClient) {}

createArbolDeObjetivos(arbolDeObjetivos: ArbolDeObjetivos) : Observable<any> {
       //return this.http.post<ArbolDeObjetivos>(this.apiUrl, arbolDeObjetivos,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, arbolDeObjetivos)
                    .pipe(map((response) => response as ArbolDeObjetivos),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

   getArbolDeObjetivos(idArbolDeObjetivos: number): Observable<ArbolDeObjetivos>{
  
      return this.http.get<ArbolDeObjetivos>(`${this.apiUrl}/${idArbolDeObjetivos}`)
      .pipe(
          catchError((e) => {
            return throwError(() => e);
          })
      );
    }

  getArbolDeObjetivosPorIdProyecto(idProyecto: number): Observable<ArbolDeObjetivos | null> {
  return this.http.get<ArbolDeObjetivos>(`${this.apiUrl}/proyecto/${idProyecto}`).pipe(
    catchError((e) => {
      return throwError(() => e);
    })
  );
}

 updateArbolDeObjetivos(arbolDeObjetivos: ArbolDeObjetivos): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${arbolDeObjetivos.id}`,
                     arbolDeObjetivos)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
}


deleteArbolDeObjetivos(idArbolDeObjetivos: number): Observable<any>{

  return this.http.delete<any>(`${this.apiUrl}/${idArbolDeObjetivos}`)
  .pipe(
    catchError(e => {
      return throwError(() => e);
    })
  );

}


}
