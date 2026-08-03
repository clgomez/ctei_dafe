import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Causa } from '../Models/causa.model';

@Injectable({
  providedIn: 'root'
})
export class CausaService {
  private apiUrl = `${environment.apiUrl}/causa`;

  constructor( private http: HttpClient) {}

createCausa(causa: Causa) : Observable<any> {
       //return this.http.post<Causa>(this.apiUrl, causa,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, causa)
                    .pipe(map((response) => response as Causa),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

  getCausasPorIdArbolDeProblemas(idArbolDeProblemas: number): Observable<Causa[] | null> {
    return this.http.get<Causa[]>(`${this.apiUrl}/arbol/${idArbolDeProblemas}`).pipe(
      catchError((e) => {
        return throwError(() => e);
      })
    );
  }  

  getCausa(idCausa: number): Observable<Causa>{

    return this.http.get<Causa>(`${this.apiUrl}/${idCausa}`)
  }

  updateCausa(causa: Causa): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${causa.id}`,
                      causa)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
  }

  deleteCausa(idCausa: number): Observable<any>{

    return this.http.delete<any>(`${this.apiUrl}/${idCausa}`)
    .pipe(
      catchError(e => {
        return throwError(() => e);
      })
    );

  }


}
