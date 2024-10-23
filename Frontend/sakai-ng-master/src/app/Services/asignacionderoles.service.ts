import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { AsignacionDeRoles } from '../Models/asignacionderoles.model';
@Injectable({
  providedIn: 'root'
})
export class AsignacionDeRolesService {
  private apiUrl = `${environment.apiUrl}/api/asignacion-roles`;

  constructor( private http: HttpClient) {}

createAsignacionDeRoles(asignacionDeRoles: AsignacionDeRoles) : Observable<AsignacionDeRoles> {

         return this.http.post<AsignacionDeRoles>(this.apiUrl,asignacionDeRoles)
                    .pipe(map((response) => response as AsignacionDeRoles),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear el producto", e.error.mensaje,"error");
                          return throwError(() => e);
                      })
             );

  }

  getAsignacionesDeRoles(): Observable <AsignacionDeRoles[]>
  {
    return this.http.get<AsignacionDeRoles[]>(this.apiUrl)
                    .pipe(map((response) => response as AsignacionDeRoles[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

/*


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

*/

  deleteAsignacionDeRoles(idAsignacionDeRoles: number): Observable<AsignacionDeRoles>{

    return this.http.delete<AsignacionDeRoles>(`${this.apiUrl}/${idAsignacionDeRoles}`)
    .pipe(
      catchError(e => {
        //console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );

  }



}
