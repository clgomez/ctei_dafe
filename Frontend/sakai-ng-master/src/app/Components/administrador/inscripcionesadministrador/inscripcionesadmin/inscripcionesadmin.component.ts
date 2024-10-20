import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Inscripcion } from 'src/app/Models/inscripcion.model';
import { InscripcionService } from 'src/app/Services/inscripcion.service';
import { InscripcionProyectoDTO } from 'src/app/Models/inscripcionproyecto-dto.model';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import { NgZone } from '@angular/core';

@Component({
  selector: 'app-inscripcionesadmin',
  templateUrl: './inscripcionesadmin.component.html',
  styleUrls: ['./inscripcionesadmin.component.css']
})
export class InscripcionesAdminComponent implements OnInit {
  currentUser: User | null = null;

  public inscripciones: Inscripcion[];

  public inscripcionProyectoDTO: InscripcionProyectoDTO = new InscripcionProyectoDTO();

  constructor(
    private inscripcionService: InscripcionService,
    private authService: AuthService,
    private router: Router,
    private cd: ChangeDetectorRef,
    private ngZone: NgZone
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.inscripcionService.getInscripciones().subscribe({
        next: (respose) =>
        { this.inscripciones = respose;

           if(this.inscripciones.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        }
        });

  }

  public aprobarInscripcionProyecto(inscripcion: Inscripcion): void{

    this.inscripcionService.getInscripcion(inscripcion.id)
    .subscribe( (inscripcionProyectoDTO) =>
        {   this.inscripcionProyectoDTO = inscripcionProyectoDTO
            console.log("fecha inscripcion",inscripcion.fechaInscripcion)
            console.log("fecha actualizacion",inscripcion.fechaActualizacion)
            console.log("proyecto id",this.inscripcionProyectoDTO.proyectoId)
            console.log("convocatoria id",this.inscripcionProyectoDTO.convocatoriaId)
            console.log("estado",this.inscripcionProyectoDTO.estado)

            this.inscripcionProyectoDTO.estado = "APROBADO";

            this.inscripcionService.updateInscripcion(inscripcion.id, this.inscripcionProyectoDTO)
                .subscribe( json => {

                //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest'])
                console.log(json.mensaje);

                const index = this.inscripciones.findIndex(i => i.id === inscripcion.id);
                if (index !== -1) {
                  this.inscripciones[index].estado = 'APROBADO';
                  this.inscripciones[index].fechaActualizacion = new Date().toISOString(); // Actualiza la fecha
                }

                this.ngZone.run(() => {
                    this.cd.detectChanges();
                  });

                })
    })

  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
