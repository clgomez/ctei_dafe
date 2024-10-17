import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../Environments/environment';
import { Notificacion } from '../Models/notificacion.model';

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private apiUrl = `${environment.apiUrl}/notificaciones`;

  constructor( private http: HttpClient) {}

  getNotificacionesPorUsuario(idNotificacion: number): Observable <Notificacion[]>
  {
    return this.http.get<Notificacion[]>(`${this.apiUrl}/usuario/${idNotificacion}`)
                    .pipe(map((response) => response as Notificacion[]),
                     catchError((e) => {
                        //console.error(e.error.mensaje);
                        //swal.fire("error al consultar proyectos en la bd", e.error.mensaje,"error");
                        return throwError(() => e);
                    })
           );
  }


  deleteNotificacion(idNotificacion: number): Observable<Notificacion>{

    return this.http.delete<Notificacion>(`${this.apiUrl}/${idNotificacion}`)
    .pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        //swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );

  }



}
