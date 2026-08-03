import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import moment from 'moment';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';


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

  validationError = '';
  globalError = '';

  constructor(private proyectoService: ProyectoService,
              private errorHandlerService: ErrorHandlerService,
              private authService: AuthService,
              private router: Router, 
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

  validarCampos(): boolean {

    const errores: string[] = [];

    const textoMin5Pattern = /^.{5,}$/;
    const textoMin10Pattern = /^.{10,}$/;
    const observacionesPattern = /^.{0,300}$/;

    // Título
    if (!this.proyecto.titulo?.trim()) {
      errores.push('El título es obligatorio');
    } else if (!textoMin5Pattern.test(this.proyecto.titulo.trim())) {
      errores.push('El título debe tener al menos 5 caracteres');
    }

    // Descripción
    if (!this.proyecto.descripcion?.trim()) {
      errores.push('La descripción es obligatoria');
    } else if (!textoMin10Pattern.test(this.proyecto.descripcion.trim())) {
      errores.push('La descripción debe tener al menos 10 caracteres');
    }

    // Población objetivo
    if (!this.proyecto.poblacionObjetivo?.trim()) {
      errores.push('La población objetivo es obligatoria');
    }

    // Justificación
    if (!this.proyecto.justificacion?.trim()) {
      errores.push('La justificación es obligatoria');
    } else if (!textoMin10Pattern.test(this.proyecto.justificacion.trim())) {
      errores.push('La justificación debe tener al menos 10 caracteres');
    }

    // Presupuesto
    if (
      this.proyecto.presupuesto === null ||
      this.proyecto.presupuesto === undefined
    ) {
      errores.push('El presupuesto es obligatorio');
    } else if (Number(this.proyecto.presupuesto) <= 0) {
      errores.push('El presupuesto debe ser mayor que cero');
    }

    // Resultados esperados
    if (!this.proyecto.resultadosEsperados?.trim()) {
      errores.push('Los resultados esperados son obligatorios');
    } else if (!textoMin10Pattern.test(this.proyecto.resultadosEsperados.trim())) {
      errores.push('Los resultados esperados deben tener al menos 10 caracteres');
    }

    // Observaciones (opcional)
    if (
      this.proyecto.observaciones &&
      !observacionesPattern.test(this.proyecto.observaciones)
    ) {
      errores.push('Las observaciones no deben exceder los 300 caracteres');
    }

    // Mostrar errores
    if (errores.length > 0) {

      console.log('Errores de validación:');
      errores.forEach(error => console.log('- ' + error));

      this.validationError = errores.join(' | '); // opcional para mostrar en pantalla

      return false;
    }

    this.validationError = '';

    return true;
  }

  public mostrarVistaPrevia(form: any): void {

    this.validationError = '';
    this.globalError = '';

    if (!this.validarCampos()) {

      if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

    this.proyectoService.validarTitulo(this.proyecto.id ?? null, this.proyecto.titulo)
        .subscribe({
          next: () => {

            localStorage.setItem(
              'proyecto',
              JSON.stringify(this.proyecto)
            );

            this.router.navigate(['/investigador/proyectosinvestigador/formvistapreviaproyectosinvest']);
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
              'Error al validar título del proyecto.'
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


  /* public mostrarVistaPrevia(): void
  {

    if (!this.validarCampos()) {
      return;
    }
    
    console.log(this.proyecto.id);

    localStorage.setItem('proyecto', JSON.stringify(this.proyecto));

    this.router.navigate(['/investigador/proyectosinvestigador/formvistapreviaproyectosinvest']);


  }*/

/*  formatDate(date: Date | null): string {
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
  }*/

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {

}


}
