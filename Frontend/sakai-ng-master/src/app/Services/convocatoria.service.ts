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

createConvocatoria(convocatoria: Convocatoria) : Observable<any> {
       //return this.http.post<Convocatoria>(this.apiUrl, convocatoria,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, convocatoria)
                    .pipe(map((response) => response as Convocatoria),
                      catchError((e) => {
                          return throwError(() => e);
                      })
             );

  }

  getConvocatorias(): Observable <Convocatoria[]>
  {
    return this.http.get<Convocatoria[]>(this.apiUrl)
                    .pipe(map((response) => response as Convocatoria[]),
                     catchError((e) => {
                        return throwError(() => e);
                    })
           );
  }

  getConvocatoria(idConvocatoria: number): Observable<Convocatoria>{

    return this.http.get<Convocatoria>(`${this.apiUrl}/${idConvocatoria}`)
  }


  getConvocatoriaPorIdProyectoYIdUsuarioInvestigador(IdProyecto: number, IdUsuarioInvestigador: number): Observable<Convocatoria>{

    return this.http.get<Convocatoria>(`${this.apiUrl}/proyecto/${IdProyecto}/usuarioinvestigador/${IdUsuarioInvestigador}`)
    .pipe(
        catchError((e) => {
          return throwError(() => e);
        })
    );
  }

  updateConvocatoria(convocatoria: Convocatoria): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${convocatoria.id}`,
                     convocatoria)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
}

  deleteConvocatoria(idConvocatoria: number): Observable<any>{

    return this.http.delete<any>(`${this.apiUrl}/${idConvocatoria}`)
    .pipe(
      catchError(e => {
        return throwError(() => e);
      })
    );
  }

}
