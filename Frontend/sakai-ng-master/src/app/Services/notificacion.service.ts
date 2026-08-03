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

  getNotificacionesPorIdUsuario(idUsuario: number): Observable <Notificacion[]>
  {
    return this.http.get<Notificacion[]>(`${this.apiUrl}/usuario/${idUsuario}`)
                    .pipe(map((response) => response as Notificacion[]),
                     catchError((e) => {
                        return throwError(() => e);
                    })
           );
  }

  updateNotificacionComoLeida(notificacionId: number): Observable<any>{

    return this.http.put<any>(`${this.apiUrl}/${notificacionId}/leida`, null)
                    .pipe(
                      catchError((e) => {
                        return throwError(() => e);
                      })
              );
}


  deleteNotificacion(idNotificacion: number): Observable<Notificacion>{

    return this.http.delete<Notificacion>(`${this.apiUrl}/${idNotificacion}`)
    .pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        return throwError(() => e);
      })
    );

  }



}
