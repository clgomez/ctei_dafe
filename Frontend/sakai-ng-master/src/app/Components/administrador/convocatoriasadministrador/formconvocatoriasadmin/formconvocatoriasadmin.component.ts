import { Component, OnInit } from '@angular/core';
import { Convocatoria } from 'src/app/Models/convocatoria.model';
import { ConvocatoriaService } from 'src/app/Services/convocatoria.service';
import { User } from 'src/app/Models/user.model';
import { AuthService } from 'src/app/Services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';


@Component({
  selector: 'app-formconvocatoriasadmin',
  templateUrl: './formconvocatoriasadmin.component.html',
  styleUrls: ['./formconvocatoriasadmin.component.css']
})
export class FormConvocatoriasAdminComponent implements OnInit {
  currentUser: User | null = null;

  convocatorias: Convocatoria[];

  public convocatoria: Convocatoria = new Convocatoria;
  public titulo: string = 'Crear Convocatoria';

  constructor(private convocatoriaService: ConvocatoriaService,
              private authService: AuthService,
              private router: Router,
              private activatedRoute: ActivatedRoute) { }
  ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarConvocatoria();


   }


   cargarConvocatoria(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.convocatoriaService.getConvocatoria(id).subscribe( (convocatoria) => this.convocatoria = convocatoria)

      }
    })
  }

   public crearConvocatoria(): void{

    this.convocatoriaService.createConvocatoria(this.convocatoria)
        .subscribe(
        (respose) =>
        {
            console.log(respose.fechaInicio);
            this.router.navigate(['/administrador/convocatoriasadministrador/convocatoriasadmin']);

        }
        )
  }

  public actualizarConvocatoria():void{

    this.convocatoriaService.updateConvocatoria(this.convocatoria)
        .subscribe( json => {
            console.log(json.mensaje);
            this.router.navigate(['/administrador/convocatoriasadministrador/convocatoriasadmin']);

      }
    )
  }

  formatDate(date: Date | null): string {
    if (!date) return '';

    //return moment(date).format('DD/MM/YYYY');
    return moment(date).format('YYYY-MM-DD');
  }

  onDateChangeFechaInicio(event: any) {
    console.log('Fecha seleccionada:', event.value);
    this.convocatoria.fechaInicio = this.formatDate(new Date(this.convocatoria.fechaInicio));

  }

  onDateChangeFechaFin(event: any) {
    console.log('Fecha seleccionada:', event.value);
    this.convocatoria.fechaFin = this.formatDate(new Date(this.convocatoria.fechaFin));

  }

   logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
