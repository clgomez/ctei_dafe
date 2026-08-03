import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Rol } from '../Models/rol.model';

@Injectable({
  providedIn: 'root'
})
export class RolService {
  private apiUrl = `${environment.apiUrl}/roles`;

  constructor( private http: HttpClient) {}

/*
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

*/
  getRoles(): Observable <Rol[]>
  {
    return this.http.get<Rol[]>(this.apiUrl)
                    .pipe(map((response) => response as Rol[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar roles en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }



  getRol(idRol: number): Observable<Rol>{

    return this.http.get<Rol>(`${this.apiUrl}/${idRol}`)
  }

  /*
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
*/


}
