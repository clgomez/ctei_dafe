import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from 'src/app/Models/proyecto.model';
import { ProyectoService } from 'src/app/Services/proyecto.service';
import { Router} from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import * as moment from 'moment';

@Component({
  selector: 'app-formvistapreviaproyectosinvest',
  templateUrl: './formvistapreviaproyectosinvest.component.html',
  styleUrls: ['./formvistapreviaproyectosinvest.component.css']
})
export class FormVistaPreviaProyectosInvestComponent implements OnInit, OnDestroy {

  currentUser: User | null = null;

  public usuario: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public titulo: string = 'Vista Previa del Proyecto';

  public pulsoSalirVistaPrevia: boolean = false;

  constructor(private proyectoService: ProyectoService,
              private router: Router,
              private authService: AuthService){}

  ngOnInit(): void {

    this.authService.currentUser.subscribe(user => this.currentUser = user );
    this.authService.getUserbyEmail(this.currentUser.username).subscribe(user => this.usuario = user );


    const datos = localStorage.getItem('proyecto');
    this.proyecto = datos ? JSON.parse(datos) : null;


  }

  public crearProyecto(): void{


    console.log('id usuario',this.usuario.id);

    this.proyecto.idUsuario = this.usuario.id;

    this.proyectoService.createProyecto(this.proyecto)
    .subscribe(
    (respose) =>
    {
      localStorage.removeItem('proyecto');
      this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
      if(respose?.titulo)
        console.log(respose.titulo);
      else  console.log('not found');
    }
    )
  }

  public actualizarProyecto():void{
    this.proyectoService.updateProyecto(this.proyecto)
        .subscribe( json => {
        localStorage.removeItem('proyecto');
        this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest'])
        console.log(json.mensaje);

      }
    )
  }

  public salirVistaPreviaProyecto():void
  {

    //console.log(this.proyecto.fechaCreacion);
    this.pulsoSalirVistaPrevia = true;
    this.router.navigate(['/investigador/proyectosinvestigador/formproyectosinvest']);

  }

  formatDate(date: Date | null): string {
    if (!date) return '';

    return moment(date).format('DD/MM/YYYY');
  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


  ngOnDestroy(): void {

    console.log(this.pulsoSalirVistaPrevia);
    console.log("destruyo vista previa");

    if(this.pulsoSalirVistaPrevia == false)
      localStorage.removeItem('proyecto');


  }

}
