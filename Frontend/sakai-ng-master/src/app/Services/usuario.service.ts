import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { User } from '../Models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuario`;

  constructor( private http: HttpClient) {}


  getUsuariosPorRol(nombreRol: string): Observable <User[]>
  {
    return this.http.get<User[]>(`${this.apiUrl}/rol/${nombreRol}`)
                    .pipe(map((response) => response as User[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }

}
