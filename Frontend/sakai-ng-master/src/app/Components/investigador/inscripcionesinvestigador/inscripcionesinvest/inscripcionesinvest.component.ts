import { Component, OnInit } from '@angular/core';
import { Inscripcion } from '@app/Models/inscripcion.model';
import { InscripcionService } from '@app/Services/inscripcion.service';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { DatePipe } from '@angular/common';
import swal from 'sweetalert2';

class InscripcionUPC
{

    id?: number;
    fechaInscripcion: string;
    fechaActualizacion: string;
    estado: string;
    usuarioId: number;
    nombreUsuario: string;
    proyectoId: number;
    tituloProyecto: string;
    convocatoriaId: number;
    tituloConvocatoria: string;

}

@Component({
  selector: 'app-inscripcionesinvest',
  templateUrl: './inscripcionesinvest.component.html',
  styleUrls: ['./inscripcionesinvest.component.css']
})
export class InscripcionesInvestComponent implements OnInit {
  currentUser: User | null = null;

  public usuario: User | null;

  public inscripciones: Inscripcion[] = [];

  public inscripcionUPC: InscripcionUPC;

  public inscripcionesUPC: InscripcionUPC[] = [];

  constructor(
    private inscripcionService: InscripcionService,
    private usuarioService: UsuarioService,
    private proyectoService: ProyectoService,
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private datePipe: DatePipe,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    console.log("username", this.currentUser.username);
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
                    (user => {this.usuario = user, this.cargarInscripcionesPorIdUsuario()});


  }

   cargarInscripcionesPorIdUsuario():void {
      
        this.inscripcionService.getInscripcionesPorIdUsuario(this.usuario.id).subscribe({
              next: (response) => {
                const inscripcionesArr = Array.isArray(response)
                  ? response
                  : [];

                this.inscripciones = inscripcionesArr.map(p => {
      
                  p.fechaInscripcion = p.fechaInscripcion
                    ? this.datePipe.transform(
                        p.fechaInscripcion,
                        'dd/MM/yyyy HH:mm:ss'
                      ) || ''
                    : '';
      
                  p.fechaActualizacion = p.fechaActualizacion
                    ? this.datePipe.transform(
                        p.fechaActualizacion,
                        'dd/MM/yyyy HH:mm:ss'
                      ) || ''
                    : '';
      
                  return {
                    ...p
                  };
                });

                this.cargarUsuarioProyectoConvocatoria();

              },
      
              error: (err) => {
                console.error(err);
                swal.fire({
                  icon: 'error',
                  title: 'Error',
                  text:
                    err.error?.mensajes?.[0]
                    || 'Error al consultar inscripciones'
                });
              }
            });
      }

  cargarUsuarioProyectoConvocatoria(): void
  {

    this.inscripciones.forEach(inscripcion => {

        this.inscripcionUPC = new InscripcionUPC();

        this.inscripcionUPC.id = inscripcion.id;
        this.inscripcionUPC.fechaInscripcion = inscripcion.fechaInscripcion;
        this.inscripcionUPC.fechaActualizacion = inscripcion.fechaActualizacion;
        this.inscripcionUPC.estado = inscripcion.estado;
        this.inscripcionUPC.usuarioId =inscripcion.usuarioId;
        this.inscripcionUPC.proyectoId = inscripcion.proyectoId;
        this.inscripcionUPC.convocatoriaId = inscripcion.convocatoriaId;

        this.inscripcionesUPC.push(this.inscripcionUPC);


    });

    this.inscripcionesUPC.forEach(inscripcionUPC => {


      this.usuarioService.getUsuario(inscripcionUPC.usuarioId).subscribe({
        next: (usuario) => {
            inscripcionUPC.nombreUsuario = `${usuario.nombre} ${usuario.apellidos}`;
        },
        error: (err) => {
          console.error(`Error al obtener el usuario ${inscripcionUPC.usuarioId}`, err);
        }
      });


        this.proyectoService.getProyecto(inscripcionUPC.proyectoId).subscribe({
            next: (proyecto) => {

                inscripcionUPC.tituloProyecto = proyecto.titulo;

            },
            error: (err) => {
              console.error(`Error al obtener el proyecto ${inscripcionUPC.proyectoId}`, err);
            }
          });


      this.convocatoriaService.getConvocatoria(inscripcionUPC.convocatoriaId).subscribe({
        next: (convocatoria) => {
            inscripcionUPC.tituloConvocatoria = convocatoria.titulo;
        },
        error: (err) => {
          console.error(`Error al obtener la convocatoria ${inscripcionUPC.convocatoriaId}`, err);
        }
      });

    });


  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
