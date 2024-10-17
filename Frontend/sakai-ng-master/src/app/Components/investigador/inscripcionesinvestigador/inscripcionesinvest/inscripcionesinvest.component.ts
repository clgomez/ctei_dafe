import { Component, OnInit } from '@angular/core';
import { Convocatoria } from 'src/app/Models/convocatoria.model';
import { ConvocatoriaService } from 'src/app/Services/convocatoria.service';
import { InscripcionProyectoDTO } from 'src/app/Models/inscripcionproyecto-dto.model';
import { InscripcionService } from 'src/app/Services/inscripcion.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import { Proyecto } from 'src/app/Models/proyecto.model';
import { ProyectoService } from 'src/app/Services/proyecto.service';

@Component({
  selector: 'app-inscripcionesinvest',
  templateUrl: './inscripcionesinvest.component.html',
  styleUrls: ['./inscripcionesinvest.component.css']
})
export class InscripcionesInvestComponent implements OnInit {
  currentUser: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public inscripcionProyectoDTO: InscripcionProyectoDTO = new InscripcionProyectoDTO();

  convocatorias: Convocatoria[];

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private inscripcionService: InscripcionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private proyectoService: ProyectoService
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarProyecto();

    this.convocatoriaService.getConvocatorias().subscribe({
        next: (respose) =>
        { this.convocatorias = respose;
          if(this.convocatorias.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });

  }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) => this.proyecto = proyecto)

      }
    })
  }

  public inscribirProyecto(convocatoria: Convocatoria): void{

    this.inscripcionProyectoDTO.proyectoId = this.proyecto.id;
    this.inscripcionProyectoDTO.convocatoriaId = convocatoria.id;

    this.inscripcionService.createInscripcion(this.inscripcionProyectoDTO)
    .subscribe(
    (respose) =>
    {
      this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
      if(respose?.titulo)
        console.log(respose.titulo);
      else  console.log('not found');
    }
    )
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
