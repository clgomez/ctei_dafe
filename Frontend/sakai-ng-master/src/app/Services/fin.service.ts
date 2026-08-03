import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Fin } from '../Models/fin.model';

@Injectable({
  providedIn: 'root'
})
export class FinService {
  private apiUrl = `${environment.apiUrl}/fines`;

  constructor( private http: HttpClient) {}

createFin(fin: Fin) : Observable<any> {
       //return this.http.post<Fin>(this.apiUrl, fin,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, fin)
                    .pipe(map((response) => response as Fin),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

 
   getFinesPorIdArbolDeObjetivos(idArbolDeObjetivos: number): Observable<Fin[] | null> {
      return this.http.get<Fin[]>(`${this.apiUrl}/arbol/${idArbolDeObjetivos}`).pipe(
        catchError((e) => {
          return throwError(() => e);
        })
      );
    }

  getFin(idFin: number): Observable<Fin>{

    return this.http.get<Fin>(`${this.apiUrl}/${idFin}`)
  }

   updateFin(fin: Fin): Observable<any>{
           return this.http.put<any>(`${this.apiUrl}/${fin.id}`,
                          fin)
                          .pipe(
                            catchError((e) => {
                               return throwError(() => e);
                            })
                    );
      }

    deleteFin(idFin: number): Observable<any>{
       
           return this.http.delete<any>(`${this.apiUrl}/${idFin}`)
           .pipe(
             catchError(e => {
               return throwError(() => e);
             })
           );
       
    }   


}
