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

  createProyecto(proyecto: Proyecto) : Observable<any> {
       //return this.http.post<Proyecto>(this.apiUrl, convocatoria,{headers: this.httpHeaders})
         return this.http.post<any>(this.apiUrl, proyecto)
                       .pipe(map((data) => data as any),
                      catchError((e) => {
                          //console.error(e.error.mensaje);
                          //swal.fire("Error al crear el proyecto", e.error.mensaje,"error");
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

  getProyectosPorIdUsuario(idUsuario: number): Observable <Proyecto[]>
  {
    return this.http.get<Proyecto[]>(`${this.apiUrl}/usuario/${idUsuario}`)
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

  updateProyecto(proyecto: Proyecto): Observable<Proyecto>{

    return this.http.put<Proyecto>(`${this.apiUrl}/${proyecto.id}`,proyecto)
                    .pipe(
                      catchError((e) => {
                        console.error(e.error.mensaje);
                        return throwError(() => e);
                      })
              );
}

  deleteProyecto(idProyecto: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${idProyecto}`)
    .pipe(
      catchError(error => {
        return throwError(() => error);
      })
    );
  } 

  validarTitulo(id: number | null, titulo: string) {

    return this.http.post(`${this.apiUrl}/validar-titulo-proyecto`,{id, titulo});
  }


  exportProyectoToExcel(id: number) {
    return this.http.get(`${this.apiUrl}/export/excel/${id}`,
      { responseType: 'blob' }
    );
  }

  exportProyectoToPdf(id: number) {
    return this.http.get(
      `${this.apiUrl}/export/pdf/${id}`,
      { responseType: 'blob' }
    );
  }

  importProyectoExcel(file: File, usuarioId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('usuarioId', usuarioId.toString());

    return this.http.post<any>(
      `${this.apiUrl}/import/excel`,
      formData
    ).pipe(

      catchError(error => {
        return throwError(() => error);
      })
    );
}

importProyectoPdf(file: File, usuarioId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('usuarioId', usuarioId.toString());

    return this.http.post<any>(
      `${this.apiUrl}/import/pdf`,
      formData
    ).pipe(

      catchError(error => {
        return throwError(() => error);
      })
    );
}

}
