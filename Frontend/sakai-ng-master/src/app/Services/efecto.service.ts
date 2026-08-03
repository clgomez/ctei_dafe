import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Efecto } from '../Models/efecto.model';

@Injectable({
  providedIn: 'root'
})
export class EfectoService {
  private apiUrl = `${environment.apiUrl}/efecto`;

  constructor( private http: HttpClient) {}

createEfecto(efecto: Efecto) : Observable<any> {
       //return this.http.post<Efecto>(this.apiUrl, efecto,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, efecto)
                    .pipe(map((response) => response as Efecto),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

  getEfectosPorIdArbolDeProblemas(idArbolDeProblemas: number): Observable<Efecto[] | null> {
  return this.http.get<Efecto[]>(`${this.apiUrl}/arbol/${idArbolDeProblemas}`).pipe(
    catchError((e) => {
       return throwError(() => e);
    })
  );
}  

  getEfecto(idEfecto: number): Observable<Efecto>{

    return this.http.get<Efecto>(`${this.apiUrl}/${idEfecto}`)
  }

  updateEfecto(efecto: Efecto): Observable<any>{
      return this.http.put<any>(`${this.apiUrl}/${efecto.id}`,
                      efecto)
                      .pipe(
                        catchError((e) => {
                          return throwError(() => e);
                        })
                );
  }

  deleteEfecto(idEfecto: number): Observable<any>{

    return this.http.delete<any>(`${this.apiUrl}/${idEfecto}`)
    .pipe(
      catchError(e => {
        return throwError(() => e);
      })
    );

  }

}
