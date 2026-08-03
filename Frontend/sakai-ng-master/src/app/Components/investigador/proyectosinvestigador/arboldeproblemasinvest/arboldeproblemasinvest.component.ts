import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { Causa } from '@app/Models/causa.model';
import { CausaService } from '@app/Services/causa.service';
import { EfectoService } from '@app/Services/efecto.service';
import { Efecto } from '@app/Models/efecto.model';
import swal from 'sweetalert2';


class ArbolDeProblemasCausasEfectos
{

  arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas() ;
  causasArbolDeProblemas: Causa[] = [];
  efectosArbolDeProblemas: Efecto[] = [];

}

@Component({
  selector: 'app-arboldeproblemasinvest',
  templateUrl: './arboldeproblemasinvest.component.html',
  styleUrls: ['./arboldeproblemasinvest.component.css']
})
export class ArbolDeProblemasInvestComponent implements OnInit{
  currentUser: User | null = null;

  public usuario: User | null;

  public visibleDetalleArbolDeProblemas: boolean = false;

  public proyecto: Proyecto = new Proyecto();

  public proyectos: Proyecto[];

  arbolDeProblemasCausasEfectosSeleccionado: ArbolDeProblemasCausasEfectos | null = null;

  public arbolDeProblemas: ArbolDeProblemas | null = null;

  public arbolDeProblemasCausasEfectos: ArbolDeProblemasCausasEfectos;

  public arbolesDeProblemasCausasEfectos: ArbolDeProblemasCausasEfectos[] = [];

 
  constructor(
    private proyectoService: ProyectoService,
    private arbolDeProblemasService: ArbolDeProblemasService,
    private causaService: CausaService,
    private efectoService: EfectoService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {

    //this.authService.currentUser.subscribe(user => this.currentUser = user);
    this.cargarProyecto();
     
  }

   cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) =>
          {this.proyecto = proyecto
            this.cargarArbolDeProblemas();
            console.log('proyecto',this.proyecto)
     
          })

      }
    })
  }

  cargarArbolDeProblemas(): void {
    console.log("id proyecto:", this.proyecto.id);
    this.arbolDeProblemasService.getArbolDeProblemasPorIdProyecto(this.proyecto.id).subscribe({
      next: (arbolDeProblemas) => {
        if (arbolDeProblemas) {
          this.arbolDeProblemas = arbolDeProblemas;
          this.cargarArbolDeProblemasCausasEfectos();
          console.log("id arbol de problemas:", this.arbolDeProblemas.id);
        } else {
          console.log("El árbol de problemas no existe para este proyecto.");
          this.arbolDeProblemas = null;
          this.arbolesDeProblemasCausasEfectos = [];
        }
      },
      error: (e) => {

         console.log('error al cargar el arbol de problemas', e);

      }
    });
  }


  cargarArbolDeProblemasCausasEfectos()
  {
     this.arbolDeProblemasCausasEfectos = new ArbolDeProblemasCausasEfectos();
     this.arbolDeProblemasCausasEfectos.arbolDeProblemas = this.arbolDeProblemas;
     this.arbolesDeProblemasCausasEfectos.push(this.arbolDeProblemasCausasEfectos);

     this.arbolesDeProblemasCausasEfectos.forEach(arbolDeProblemasCausasEfectos => {

      console.log('id del arbol de problemas:',this.arbolDeProblemas.id);
       
      this.causaService.getCausasPorIdArbolDeProblemas(this.arbolDeProblemas.id).subscribe({
            next: (causas) => {
                 
                arbolDeProblemasCausasEfectos.causasArbolDeProblemas =  Array.isArray(causas) ? causas : [];

            },
            error: (err) => {
              //console.error(`Error al obtener causas`, err);
            }
          });
      this.efectoService.getEfectosPorIdArbolDeProblemas(this.arbolDeProblemas.id).subscribe({
            next: (efectos) => {

                arbolDeProblemasCausasEfectos.efectosArbolDeProblemas = Array.isArray(efectos) ? efectos : [];

            },
            error: (err) => {
              //console.error(`Error al obtener efectos`, err);
            }
          });    
    });
  }

  get existeArbolDeProblemas(): boolean {
    return this.arbolDeProblemas !== null;
}

  
  public asociarIdProyectoConArbolDeProblemas()
  {
    console.log('id del proyecto hola:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/formarboldeproblemasinvest/proyecto', this.proyecto.id]);

  }

  
mostrarDetalleArbolDeProblemas(arbolDeProblemasCausasEfectos: ArbolDeProblemasCausasEfectos):void
   {
    this.arbolDeProblemasCausasEfectosSeleccionado = arbolDeProblemasCausasEfectos;
    this.visibleDetalleArbolDeProblemas = true;
   }

eliminarArbolDeProblemas(arbolDeProblemasCE: ArbolDeProblemasCausasEfectos): void {
    swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea eliminar el árbol de problemas: ${arbolDeProblemasCE.arbolDeProblemas.descripcion}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, eliminar!',
      cancelButtonText: 'No, cancelar!',
      customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-danger',

      },
      buttonsStyling: false,
      reverseButtons: true
    } as any).then((result) => {
      if (result.value) {
        // eliminando arbol de problemas, causas y efectos en cascada
       this.arbolDeProblemasService.deleteArbolDeProblemas(arbolDeProblemasCE.arbolDeProblemas.id).subscribe({
                next: (arbolDeProblemas) => {

                  this.arbolesDeProblemasCausasEfectos = [];
                  this.arbolDeProblemas = null;
                  this.cargarArbolDeProblemas();
                  //console.log('árbol de problemas eliminado: ',arbolDeProblemasCE.arbolDeProblemas.id);
                  swal.fire(
                                'Árbol de Problemas Eliminado!',
                                `Árbol de Problemas: ${arbolDeProblemasCE.arbolDeProblemas.descripcion} eliminado con éxito.`,
                                'success'
                            )
                },
                error: (err) => {
                  console.error(`Error al eliminar árbol de problemas`, err);
                }
              });    

      }else
      {
        swal.fire(
              'Eliminación cancelada!',
              `Eliminación del árbol de problemas: ${arbolDeProblemasCE.arbolDeProblemas.descripcion} cancelado`,
              'info'
        )

      }
    })
  }

  public irAlArbolDeObjetivos()
  {
    console.log('id del proyecto en árbol de objetivos:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest', this.proyecto.id]);

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
