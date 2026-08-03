import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model';
import { AsignacionDeRoles } from '@app/Models/asignacionderoles.model';
import { AsignacionDeRolesService } from '@app/Services/asignacionderoles.service';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Proyecto } from '@app/Models/proyecto.model';
import { UsuarioService } from '@app/Services/usuario.service';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import swal from 'sweetalert2';


@Component({
  selector: 'app-formasignacionderolesadmin',
  templateUrl: './formasignacionderolesadmin.component.html',
  styleUrls: ['./formasignacionderolesadmin.component.css']
})
export class FormAsignacionDeRolesAdminComponent implements OnInit {
  currentUser: User | null = null;

  usuariosTutores: User[];

  usuariosEvaluadores: User[];

  public titulo: string = 'Asignación de Roles';

  public asignacionDeRoles: AsignacionDeRoles = new AsignacionDeRoles;

  public proyecto: Proyecto = new Proyecto;

  validationError = '';
  globalError = '';

  constructor(
    private usuarioService: UsuarioService,
    private asignacionDeRolesService: AsignacionDeRolesService,
    private proyectoService: ProyectoService,
    private errorHandlerService: ErrorHandlerService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    

    this.asignacionDeRoles.id = null;
    this.asignacionDeRoles.usuarioTutorId = null;
    this.asignacionDeRoles.usuarioEvaluadorId = null;

    this.determinarSegmentosUrl();
    this.cargarTutores();
    this.cargarEvaluadores();
  }


   public guardarAsignacionDeRoles(form: any): void {

    if (this.asignacionDeRoles?.id) {
      this.actualizarAsignacionDeRoles(form);
    } else {
      this.crearAsignacionDeRoles(form);
    }

 }

  determinarSegmentosUrl():void
  {

    this.activatedRoute.parent.url.subscribe(urlSegments => {

    console.log('segmentos:', urlSegments);
    if (urlSegments.length > 0) {
        const segment = urlSegments[1].path; // El segundo segmento de la URL
        console.log('segmento:', segment);
        if (segment === 'proyecto') {
          // Si la URL es /formasignacionderolesadmin/proyecto/:id
          //console.log('id de proyecto');
          this.cargarProyecto();

        } else if (segment === 'asignacion') {
          // Si la URL es /formasignacionderolesadmin/asignacion/:id
          //console.log('id de asignacion');
          this.cargarAsignacionDeRoles();

        }
      }

  });

  }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.proyectoService.getProyecto(id).
        subscribe((proyecto) =>{
                    this.proyecto = proyecto;
                   
       } )

      }
    })
  }

  cargarAsignacionDeRoles(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.asignacionDeRolesService.getAsignacionDeRoles(id).
        subscribe( (asignacionDeRoles) => {
            this.asignacionDeRoles = asignacionDeRoles
        })
      }
    })
  }


  cargarTutores(): void
  {
    this.usuarioService.getUsuariosPorRol('ROL_TUTOR').subscribe({
        next: (respose) =>
        {
            //console.log(respose);
            this.usuariosTutores = respose;
            //console.log(respose.length);
          if(this.usuariosTutores.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });
  }

  cargarEvaluadores(): void
  {
    this.usuarioService.getUsuariosPorRol('ROL_EVALUADOR').subscribe({
        next: (respose) =>
        { this.usuariosEvaluadores = respose;
           // console.log(respose.length);
          if(this.usuariosEvaluadores.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar productos en la bd", err.error.mensaje,"error");
        //}
        });
  }


  public crearAsignacionDeRoles(form: any): void{

    this.asignacionDeRoles.proyectoId = this.proyecto.id;
    this.asignacionDeRoles.estado = 'ACTIVO';
    console.log('proyecto id:', this.asignacionDeRoles.proyectoId);
    console.log('asignacion id:', this.asignacionDeRoles.id);
    this.asignacionDeRolesService.createAsignacionDeRoles(this.asignacionDeRoles)
        .subscribe({
           next: (data: any) => 
          {
            swal.fire('Nueva asignación de roles',`Asignación de roles creada con éxito!`, 'success');
            this.router.navigate(['/administrador/asignacionderolesadministrador/asignacionderolesadmin']);
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
                'Error al crear asignación de roles.'
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
          }
        });

  }


  public actualizarAsignacionDeRoles(form: any):void{
  
    swal.fire({
               title: 'Está seguro?',
               text: `¿Seguro que desea actualizar esta asignación de roles?`,
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
              }as any).then((result) => {
              if (result.value) {
                //this.asignacionDeRoles.proyectoId = this.proyecto.id;
                console.log('asignacion id:', this.asignacionDeRoles.id);
                console.log('proyecto id:', this.asignacionDeRoles.proyectoId);

                this.asignacionDeRolesService.updateAsignacionDeRoles(this.asignacionDeRoles)
                    .subscribe({
                        next: (data) => { 
                        console.log('asignacion de roles actualizada');
                        swal.fire('Asignacion de roles actualizada',`Asignacion de roles actualizada con éxito!`, 'success');
                        this.router.navigate(['/administrador/asignacionderolesadministrador/asignacionderolesadmin']);
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
                          'Error al actualizar asignación de roles.'
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
                      }
                  });
             }else
              {
                swal.fire(
                  'Actualización cancelada!',
                  `Actualización de la asignación de roles cancelada`,
                  'info'
                  )
              }
            })

  }

    public cancelarAsignacionDeRoles(asignacionDeRoles :AsignacionDeRoles):void
    {
  
      if(!asignacionDeRoles.id)
      {
        swal.fire(
                'Creación cancelada!',
                `Creación de la asignación de roles cancelada`,
                'info'
                )
      }
      else
      {
        swal.fire(
                'Actualización cancelada!',
                `Actualización de la asignación de roles cancelada`,
                'info'
                )
      }
      this.router.navigate(['/administrador/asignacionderolesadministrador/asignacionderolesadmin']);
    }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
