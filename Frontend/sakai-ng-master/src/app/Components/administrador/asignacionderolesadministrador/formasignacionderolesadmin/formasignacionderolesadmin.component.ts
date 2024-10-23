import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';
import { AsignacionDeRoles } from 'src/app/Models/asignacionderoles.model';
import { AsignacionDeRolesService } from 'src/app/Services/asignacionderoles.service';
import { ProyectoService } from 'src/app/Services/proyecto.service';
import { Proyecto } from 'src/app/Models/proyecto.model';
import { UsuarioService } from 'src/app/Services/usuario.service';


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


  constructor(
    private usuarioService: UsuarioService,
    private asignacionDeRolesService: AsignacionDeRolesService,
    private proyectoService: ProyectoService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.asignacionDeRoles.usuarioTutorId = null;
    this.asignacionDeRoles.usuarioEvaluadorId = null;

    this.cargarProyecto();
    this.cargarTutores();
    this.cargarEvaluadores();


  }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.proyectoService.getProyecto(id).subscribe( (proyecto) => this.proyecto = proyecto)

      }
    })
  }

  cargarTutores(): void
  {
    this.usuarioService.getUsuariosPorRol('ROL_TUTOR').subscribe({
        next: (respose) =>
        {
            console.log(respose);
            this.usuariosTutores = respose;
            console.log(respose.length);
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
            console.log(respose.length);
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


  public crearAsignacionDeRoles(): void{

    this.asignacionDeRoles.proyectoId = this.proyecto.id;

    this.asignacionDeRolesService.createAsignacionDeRoles(this.asignacionDeRoles)
        .subscribe(
        (respose) =>
        {
            console.log(respose);
            this.router.navigate(['/administrador/asignacionderolesadministrador/asignacionderolesadmin']);

        }
        )
  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
