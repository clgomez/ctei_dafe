import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { InscripcionService } from '@app/Services/inscripcion.service';
import { Inscripcion } from '@app/Models/inscripcion.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { Usuario } from '@app/Models/usuario.model';
import { UsuarioService } from '@app/Services/usuario.service';

class UsuarioProyectoConvocatoria
{

    nombresapellidosUsuario: string;
    proyecto: Proyecto = new Proyecto();
    tituloConvocatoria: string;
    usuarioId: number;
    proyectoId: number;
    convocatoriaId: number;

}

@Component({
  selector: 'app-proyectoseval',
  templateUrl: './proyectoseval.component.html',
  styleUrls: ['./proyectoseval.component.css']
})
export class ProyectosEvalComponent implements OnInit {
  currentUser: User | null = null;

   public usuarioEvaluador: Usuario | null;

  public visibleDetalleProyecto: boolean = false;

  public proyectos: Proyecto[];

  proyectoSeleccionado: Proyecto | null = null;

  public usuarioproyectoconvocatoria: UsuarioProyectoConvocatoria | null = null;

  public usuariosproyectosconvocatorias: UsuarioProyectoConvocatoria[] = [];

  public inscripciones: Inscripcion[] = [];

  constructor(
    private inscripcionService: InscripcionService,
    private usuarioService: UsuarioService,
    private proyectoService: ProyectoService,
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private datePipe: DatePipe,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {

    this.cargarUsuarioEvaluador();
    this.cargarInscripciones();

  }

   cargarUsuarioEvaluador(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['idusuarioevaluador']
      if(id){
        this.usuarioService.getUsuarioPorId(id).subscribe( (usuarioEvaluador) =>
          {this.usuarioEvaluador = usuarioEvaluador;
           
            console.log('usuario evaluador',this.usuarioEvaluador)
     
          })

      }
    })
  }

  cargarInscripciones():void
  {
    this.inscripcionService.getInscripciones().subscribe({
        next: (inscripciones) =>
        { this.inscripciones = inscripciones;
            console.log(this.inscripciones);
          this.cargarUsuarioProyectoConvocatoria();
           if(this.inscripciones.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          console.log(err.error.mensaje)
          //swal.fire("error al consultar inscripciones en la bd", err.error.mensaje,"error");
        }
        });

  }

  cargarUsuarioProyectoConvocatoria(): void
  {
    this.inscripciones.forEach(inscripcion => {

        this.usuarioproyectoconvocatoria = new UsuarioProyectoConvocatoria();
        this.usuarioproyectoconvocatoria.usuarioId = inscripcion.usuarioId;
        this.usuarioproyectoconvocatoria.proyectoId = inscripcion.proyectoId;
        this.usuarioproyectoconvocatoria.convocatoriaId = inscripcion.convocatoriaId;
        this.usuariosproyectosconvocatorias.push(this.usuarioproyectoconvocatoria);

    });

    this.usuariosproyectosconvocatorias.forEach(usuarioproyectoconvocatoria => {

        this.usuarioService.getUsuario(usuarioproyectoconvocatoria.usuarioId).subscribe({
            next: (usuario) => {
                //console.log('usuario:', usuario)
                usuarioproyectoconvocatoria.nombresapellidosUsuario = `${usuario.nombre} ${usuario.apellidos}`;
            },
            error: (err) => {
              console.error(`Error al obtener el usuario ${usuarioproyectoconvocatoria.usuarioId}`, err);
            }
          });

        this.proyectoService.getProyecto(usuarioproyectoconvocatoria.proyectoId).subscribe({
            next: (proyecto) => {

              //console.log('proyecto:', proyecto)
                usuarioproyectoconvocatoria.proyecto = proyecto;

                 proyecto.fechaCreacion = proyecto.fechaCreacion
                  ? this.datePipe.transform(
                      proyecto.fechaCreacion,
                      'dd/MM/yyyy HH:mm:ss'
                    ) || ''
                  : '';
                
                proyecto.fechaActualizacion = proyecto.fechaActualizacion
                  ? this.datePipe.transform(
                      proyecto.fechaActualizacion,
                      'dd/MM/yyyy HH:mm:ss'
                    ) || ''
                  : '';


            },
            error: (err) => {
              console.error(`Error al obtener el proyecto ${usuarioproyectoconvocatoria.proyectoId}`, err);
            }
          });


       this.convocatoriaService.getConvocatoria(usuarioproyectoconvocatoria.convocatoriaId).subscribe({
            next: (convocatoria) => {
              //console.log('convocatoria:', convocatoria)
               usuarioproyectoconvocatoria.tituloConvocatoria = convocatoria.titulo;
           },
           error: (err) => {
          console.error(`Error al obtener la convocatoria ${usuarioproyectoconvocatoria.convocatoriaId}`, err);
          }
         });


    });


  }

  mostrarDetalleProyecto(proyecto: Proyecto):void
   {
    this.proyectoSeleccionado = proyecto;
    this.visibleDetalleProyecto = true;
   }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
