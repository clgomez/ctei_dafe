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

  getAsignacionesDeRolesPorIdUsuarioInvestigador(idUsuarioInvestigador: number): Observable <AsignacionDeRoles[]>
  {
    return this.http.get<AsignacionDeRoles[]>(`${this.apiUrl}/usuarioinvestigador/${idUsuarioInvestigador}`)
                    .pipe(map((response) => response as AsignacionDeRoles[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar asignaciones de roles en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

  getAsignacionesDeRolesPorIdUsuarioTutor(idUsuarioTutor: number): Observable <AsignacionDeRoles[]>
  {
    return this.http.get<AsignacionDeRoles[]>(`${this.apiUrl}/usuariotutor/${idUsuarioTutor}`)
                    .pipe(map((response) => response as AsignacionDeRoles[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar asignaciones de roles en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }


  getAsignacionDeRoles(idAsignacionDeRoles: number): Observable<AsignacionDeRoles>{

    return this.http.get<AsignacionDeRoles>(`${this.apiUrl}/${idAsignacionDeRoles}`)
  }


  getAsignacionDeRolesPorIdProyecto(idProyecto: number): Observable <AsignacionDeRoles>
  {
    return this.http.get<AsignacionDeRoles>(`${this.apiUrl}/proyecto/${idProyecto}`)
                    .pipe(map((response) => response as AsignacionDeRoles),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar asignaciones de roles en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }




  updateAsignacionDeRoles(asignacionDeRoles: AsignacionDeRoles): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${asignacionDeRoles.id}`,asignacionDeRoles)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}



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
