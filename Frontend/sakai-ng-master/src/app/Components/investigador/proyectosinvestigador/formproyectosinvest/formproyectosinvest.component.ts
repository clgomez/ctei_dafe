import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from 'src/app/Models/proyecto.model';
import { ProyectoService } from 'src/app/Services/proyecto.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import * as moment from 'moment';


@Component({
  selector: 'app-formproyectosinvest',
  templateUrl: './formproyectosinvest.component.html',
  styleUrls: ['./formproyectosinvest.component.css']


})

export class FormProyectosInvestComponent implements OnInit,OnDestroy{

  currentUser: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public titulo: string = 'Formular Proyecto';

  public fechaCreacion: string = null;

  constructor(private proyectoService: ProyectoService,
              private router: Router, private authService: AuthService,
              private activatedRoute: ActivatedRoute){}

  ngOnInit(): void {

   this.authService.currentUser.subscribe(user => this.currentUser = user);

   this.cargarProyecto();

    const datos = localStorage.getItem('proyecto');

    if (datos) {
      this.proyecto = JSON.parse(datos);
      localStorage.removeItem('proyecto');

    }

  }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) => this.proyecto = proyecto)

      }
    })
  }

  public mostrarVistaPrevia(): void
  {

    console.log(this.proyecto.id);

    localStorage.setItem('proyecto', JSON.stringify(this.proyecto));

    this.router.navigate(['/investigador/proyectosinvestigador/formvistapreviaproyectosinvest']);


  }

  formatDate(date: Date | null): string {
    if (!date) return '';

    return moment(date).format('DD/MM/YYYY');
  }

  onDateChangeFechaCreacion(event: any) {
    console.log('Fecha seleccionada:', event.value);
    this.proyecto.fechaCreacion = this.formatDate(new Date(this.proyecto.fechaCreacion));
  }

  onDateChangeFechaActualizacion(event: any) {
    console.log('Fecha seleccionada:', event.value);
    this.proyecto.fechaActualizacion = this.formatDate(new Date(this.proyecto.fechaActualizacion));
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {

}


}
