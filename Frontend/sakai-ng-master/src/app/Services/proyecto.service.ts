import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Proyecto } from '../Models/proyecto.model';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {
  private apiUrl = `${environment.apiUrl}/proyecto`;

  constructor( private http: HttpClient) {}

createProyecto(proyecto: Proyecto) : Observable<Proyecto> {
       //return this.http.post<Convocatoria>(this.apiUrl, convocatoria,{headers: this.httpHeaders})
         return this.http.post<Proyecto>(this.apiUrl, proyecto)
                    .pipe(map((response) => response as Proyecto),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear el producto", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );

  }

  getProyectos(): Observable <Proyecto[]>
  {
    return this.http.get<Proyecto[]>(this.apiUrl)
                    .pipe(map((response) => response as Proyecto[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

  getProyecto(idProyecto: number): Observable<Proyecto>{

    return this.http.get<Proyecto>(`${this.apiUrl}/${idProyecto}`)
  }

  updateProyecto(proyecto: Proyecto): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${proyecto.id}`,proyecto)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}

  deleteProyecto(idProyecto: number): Observable<Proyecto>{

    return this.http.delete<Proyecto>(`${this.apiUrl}/${idProyecto}`)
    .pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );

  }



}
