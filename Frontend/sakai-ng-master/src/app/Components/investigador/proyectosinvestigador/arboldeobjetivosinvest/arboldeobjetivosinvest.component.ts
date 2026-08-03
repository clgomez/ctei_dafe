import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { ArbolDeObjetivos } from '@app/Models/arboldeobjetivos.model';
import { ArbolDeObjetivosService } from '@app/Services/arboldeobjetivos.service';
import { Causa } from '@app/Models/causa.model';
import { CausaService } from '@app/Services/causa.service';
import { Efecto } from '@app/Models/efecto.model';
import { EfectoService } from '@app/Services/efecto.service';
import { Medio } from '@app/Models/medio.model';
import { MedioService } from '@app/Services/medio.service';
import { Fin } from '@app/Models/fin.model';
import { FinService } from '@app/Services/fin.service';
import swal from 'sweetalert2';


class ArbolDeObjetivosMediosFines
{

  arbolDeObjetivos: ArbolDeObjetivos = new ArbolDeObjetivos() ;
  mediosArbolDeObjetivos: Medio[] = [];
  finesArbolDeObjetivos: Fin[] = [];

}

@Component({
  selector: 'app-arboldeobjetivosinvest',
  templateUrl: './arboldeobjetivosinvest.component.html',
  styleUrls: ['./arboldeobjetivosinvest.component.css']
})
export class ArbolDeObjetivosInvestComponent implements OnInit{
  currentUser: User | null = null;

  public usuario: User | null;

  public visibleDetalleArbolDeObjetivos: boolean = false;

  public proyecto: Proyecto = new Proyecto();

  public proyectos: Proyecto[];

  arbolDeObjetivosMediosFinesSeleccionado: ArbolDeObjetivosMediosFines | null = null;

  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();

  public arbolDeObjetivos: ArbolDeObjetivos | null = null;
  
  public arbolDeObjetivosMediosFines: ArbolDeObjetivosMediosFines;

  public arbolesDeObjetivosMediosFines: ArbolDeObjetivosMediosFines[] = [];

 
  constructor(
    private proyectoService: ProyectoService,
    private arbolDeProblemasService: ArbolDeProblemasService,
    private arbolDeObjetivosService: ArbolDeObjetivosService,
    private causaService: CausaService,
    private efectoService: EfectoService,
    private medioService: MedioService,
    private finService: FinService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
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
          {this.proyecto = proyecto;
            this.cargarArbolDeObjetivos();
            console.log('proyecto',this.proyecto);
     
          })

      }
    })
  }

 
    cargarArbolDeObjetivos(): void {

    this.arbolDeObjetivosService.getArbolDeObjetivosPorIdProyecto(this.proyecto.id).subscribe({
      next: (arbolDeObjetivos) => {
        if (arbolDeObjetivos) {
            this.arbolDeObjetivos = arbolDeObjetivos;
            this.cargarArbolDeObjetivosMediosFines();
            console.log("Árbol de objetivos cargado:", this.arbolDeObjetivos.id);
        } else {
          console.log("El árbol de objetivos no existe para este proyecto.");
          this.arbolDeObjetivos = null;
          this.arbolesDeObjetivosMediosFines = [];
        }
      },
      error: (e) => {
         console.log('error al cargar el arbol de objetivos', e);

      }
    });
  }


  cargarArbolDeObjetivosMediosFines()
  {
     this.arbolDeObjetivosMediosFines = new ArbolDeObjetivosMediosFines();
     this.arbolDeObjetivosMediosFines.arbolDeObjetivos = this.arbolDeObjetivos;
     this.arbolesDeObjetivosMediosFines.push(this.arbolDeObjetivosMediosFines);

     this.arbolesDeObjetivosMediosFines.forEach(arbolDeObjetivosMediosFines => {

      console.log('id del arbol de objetivos:',this.arbolDeObjetivos.id);
       
      this.medioService.getMediosPorIdArbolDeObjetivos(this.arbolDeObjetivos.id).subscribe({
            next: (medios) => {
                 
                arbolDeObjetivosMediosFines.mediosArbolDeObjetivos =  Array.isArray(medios) ? medios : [];

            },
            error: (err) => {
              //console.error(`Error al obtener medios`, err);
            }
          });
      this.finService.getFinesPorIdArbolDeObjetivos(this.arbolDeObjetivos.id).subscribe({
            next: (fines) => {

                arbolDeObjetivosMediosFines.finesArbolDeObjetivos = Array.isArray(fines) ? fines : [];

            },
            error: (err) => {
              //console.error(`Error al obtener fines`, err);
            }
          });    
    });
  }

  get existeArbolDeObjetivos(): boolean {
  return this.arbolDeObjetivos !== null;
}

  public asociarIdProyectoConArbolDeProblemas()
  {
    console.log('id del proyecto hola:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/formarboldeproblemasinvest/proyecto', this.proyecto.id]);

  }

  
mostrarDetalleArbolDeObjetivos(arbolDeObjetivosMediosFines: ArbolDeObjetivosMediosFines):void
   {
    this.arbolDeObjetivosMediosFinesSeleccionado = arbolDeObjetivosMediosFines;
    this.visibleDetalleArbolDeObjetivos = true;
   }

eliminarArbolDeObjetivos(arbolDeObjetivosMF: ArbolDeObjetivosMediosFines): void {
    swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea eliminar el árbol de objetivos: ${arbolDeObjetivosMF.arbolDeObjetivos.descripcion}?`,
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
        // eliminando arbol de objetivos, medios y fines en cascada
       this.arbolDeObjetivosService.deleteArbolDeObjetivos(arbolDeObjetivosMF.arbolDeObjetivos.id).subscribe({
                next: (arbolDeObjetivos) => {

                  this.arbolesDeObjetivosMediosFines = [];
                  this.arbolDeObjetivos = null; 
                  this.cargarArbolDeObjetivos();
                  //console.log('árbol de objetivos eliminado: ',arbolDeObjetivosMF.arbolDeObjetivos.id);
                  swal.fire(
                                'Árbol de Objetivos Eliminado!',
                                `Árbol de Objetivos: ${arbolDeObjetivosMF.arbolDeObjetivos.descripcion} eliminado con éxito.`,
                                'success'
                            )


                },
                error: (err) => {
                  console.error(`Error al eliminar árbol de objetivos`, err);
                }
              });    

      }else
      {
        swal.fire(
              'Eliminación cancelada!',
              `Eliminación del árbol de objetivos: ${arbolDeObjetivosMF.arbolDeObjetivos.descripcion} cancelado`,
              'info'
        )

      }
    })
  }

  public generarArbolDeObjetivos()
  {
    console.log('id del proyecto:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/formarboldeobjetivosinvest/proyecto', this.proyecto.id]);

  }

  public regresarAlArbolDeProblemas()
  {
    console.log('id del proyecto en árbol de objetivos:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/arboldeproblemasinvest', this.proyecto.id]);

  }

   public irALasActividadesDelProyecto()
  {
    //console.log('id del proyecto en árbol de objetivos:',this.proyecto.id);
    this.router.navigate(['/investigador/proyectosinvestigador/actividadesinvest', this.proyecto.id]);

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
