import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Router} from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import moment from 'moment';
import swal from 'sweetalert2';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

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

  validationError = '';
  globalError = '';

  constructor(private proyectoService: ProyectoService,
              private router: Router,
              private authService: AuthService,
              private errorHandlerService: ErrorHandlerService
              ){}

  ngOnInit(): void {

    this.authService.currentUser.subscribe(user => this.currentUser = user );
    this.authService.getUserbyEmail(this.currentUser.username).subscribe(user => this.usuario = user );

    const datos = localStorage.getItem('proyecto');
    this.proyecto = datos ? JSON.parse(datos) : null;


  }

  public guardarProyecto(form: any): void {

    if (this.proyecto?.id) {
      this.actualizarProyecto(form);
    } else {
      this.crearProyecto(form);
    }

 }

  public crearProyecto(form: any): void{


    console.log('id usuario',this.usuario.id);

    this.proyecto.idUsuario = this.usuario.id;
    this.proyecto.estado = 'ACTIVO';

    this.proyectoService.createProyecto(this.proyecto).subscribe({
      next: (data: any) => 
      {
        localStorage.removeItem('proyecto');
        this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

        //console.log('valor de data:', data)
        if(data.proyecto?.titulo)
        {  
          console.log(data.proyecto.titulo);
          swal.fire('Nuevo proyecto',`Proyecto: ${data.proyecto.titulo} creado con éxito!`, 'success');
        }
        else  console.log('not found');
      },
      error: (error) => {
        console.log('Error completo:', error);
  
          this.validationError = '';
          this.globalError = '';

          console.log('mensajeGlobal');
          console.log(error.mensajeGlobal);
        
          this.validationError = this.errorHandlerService.procesarError(
            error,
            form,
            'Error al crear el proyecto.'
          );
          
         this.globalError = error.mensajeGlobal;

         console.log('Mensaje final:');
         console.log(this.validationError);
     

           // limpiar mensaje global
          if (this.validationError) {
            setTimeout(() => {
              this.validationError = '';
            }, 4000);
          }

           if (this.globalError) {
            setTimeout(() => {
              this.globalError = '';
            }, 4000);
          }
      }//fin error
    });
  }

 
  public actualizarProyecto(form: any):void{
     swal.fire({
          title: 'Está seguro?',
          text: `¿Seguro que desea actualizar el proyecto: ${this.proyecto.titulo}?`,
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#007bff',
          confirmButtonText: 'Si, actualizar!',
          cancelButtonText: 'No, cancelar!',
          customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger',
    
          },
          buttonsStyling: false,
          reverseButtons: true
        } as any).then((result) => {
          if (result.value) {
            this.proyectoService.updateProyecto(this.proyecto).subscribe({
               next: (data: any) => 
               { localStorage.removeItem('proyecto');
                this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest'])
                swal.fire('Proyecto actualizado',`Proyecto: ${data.proyecto.titulo} actualizado con éxito!`, 'success');
               },
               error: (error) => 
               {
                console.log('Error completo:', error);
  
                this.validationError = '';
                this.globalError = '';

                console.log('mensajeGlobal');
                console.log(error.mensajeGlobal);
              
                this.validationError = this.errorHandlerService.procesarError(
                  error,
                  form,
                  'Error al actualizar el proyecto.'
                );

                this.globalError = error.mensajeGlobal;
              
                console.log('Mensaje final:');
                console.log(this.validationError);
      
                // limpiar mensaje global
                if (this.validationError) {
                  setTimeout(() => {
                    this.validationError = '';
                  }, 4000);
                }

                 if (this.globalError) {
                  setTimeout(() => {
                    this.globalError = '';
                  }, 4000);
                }

               }//fin error
            });
     }else
      {
        swal.fire(
          'Actualización cancelada!',
          `Actualización del proyecto: ${this.proyecto.titulo} cancelada`,
          'info'
          )
    
      }
    })
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
