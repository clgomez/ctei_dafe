import { Component, OnInit } from '@angular/core';
import { Convocatoria } from '@app/Models/convocatoria.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { User } from '@app/Models/user.model';
import { AuthService } from '@app/Services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import moment from 'moment';
import swal from 'sweetalert2';
import { formatDate } from '@angular/common';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

@Component({
  selector: 'app-formconvocatoriasadmin',
  templateUrl: './formconvocatoriasadmin.component.html',
  styleUrls: ['./formconvocatoriasadmin.component.css']
})
export class FormConvocatoriasAdminComponent implements OnInit {
  currentUser: User | null = null;

  convocatorias: Convocatoria[];

  public convocatoria: Convocatoria = new Convocatoria;
  public titulo: string = 'Convocatoria';

    // Aquí declaras la variable para el placeholder dinámico
  public placeholderFechaInicio: string = 'dd/mm/yyyy';
  public placeholderFechaFin: string = 'dd/mm/yyyy';

  // 🔹 Fechas mínimas en los calendarios
  minDateInicio: Date = new Date();
  minDateFin: Date = new Date();

  // errores de validación personalizados
  public erroresFechas = {
    inicio: '',
    fin: ''
  };

  public fechasValidas: boolean = true;

   validationError = '';
   globalError = '';

  constructor(private convocatoriaService: ConvocatoriaService,
              private authService: AuthService,
              private router: Router,
              private errorHandlerService: ErrorHandlerService,
              private activatedRoute: ActivatedRoute) { }
  ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarConvocatoria();


   }

  public guardarConvocatoria(form: any): void {

      if (this.convocatoria?.id) {
        this.actualizarConvocatoria(form);
      } else {
        this.crearConvocatoria(form);
      }

  }

   cargarConvocatoria(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        console.log('id', id);
        this.convocatoriaService.getConvocatoria(id).subscribe( (convocatoria) => {
          this.convocatoria = convocatoria
          
                 // 🔹 Si el backend manda fechas como string, convierto a Date
          if (this.convocatoria.fechaInicio) {
            this.convocatoria.fechaInicio = new Date(this.convocatoria.fechaInicio + 'T00:00:00');
            this.placeholderFechaInicio = formatDate(this.convocatoria.fechaInicio, 'dd/MM/yy', 'en-US');
            this.minDateFin = this.addOneDay(this.convocatoria.fechaInicio);
          }
          if (this.convocatoria.fechaFin) {
            this.convocatoria.fechaFin = new Date(this.convocatoria.fechaFin + 'T00:00:00');
            this.placeholderFechaFin = formatDate(this.convocatoria.fechaFin, 'dd/MM/yy', 'en-US');
          }
        })

      }
    })
  }

  validarCampos(): boolean {

    const errores: string[] = [];

    const textoMin5Pattern = /^.{5,}$/;
    const textoMin10Pattern = /^.{10,}$/;
    
    // Título
    if (!this.convocatoria.titulo?.trim()) {
      errores.push('El título de la convocatoria es obligatorio');
    } else if (!textoMin5Pattern.test(this.convocatoria.titulo.trim())) {
      errores.push('El título de la convocatoria debe tener al menos 5 caracteres');
    }

    // Descripción
    if (!this.convocatoria.descripcion?.trim()) {
      errores.push('La descripción de la convocatoria es obligatoria');
    } else if (!textoMin10Pattern.test(this.convocatoria.descripcion.trim())) {
      errores.push('La descripción de la convocatoria debe tener al menos 10 caracteres');
    }

    // Fecha Inicio
    if (!this.convocatoria.fechaInicio) {
      errores.push('La fecha de inicio de la convocatoria es obligatoria');
    }

     // Fecha Fin
    if (!this.convocatoria.fechaFin) {
      errores.push('La fecha fin de la convocatoria es obligatoria');
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


  // 🔹 Validación de fechas
  validarFechas(): boolean {

    const errores: string[] = [];

    console.log('Dentro de validar fechas');
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    this.erroresFechas = { inicio: '', fin: '' };
    this.fechasValidas = true;

    console.log('fechaInicio', this.convocatoria.fechaInicio);
    console.log('fechaFin', this.convocatoria.fechaFin);

    if (!this.convocatoria.fechaInicio || !this.convocatoria.fechaFin) {
      this.fechasValidas = false;
      return false;
    }

    const inicio = new Date(this.convocatoria.fechaInicio);
    const fin = new Date(this.convocatoria.fechaFin);

    if (inicio < hoy) {
      //swal.fire('Error', 'La fecha de inicio no puede ser anterior a hoy.', 'error');
      this.erroresFechas.inicio = 'La fecha de inicio no puede ser anterior a hoy.';
      errores.push(this.erroresFechas.inicio);
      this.fechasValidas = false;
      //return false;
    }

    if (fin <= hoy) {
      //swal.fire('Error', 'La fecha de finalización debe ser posterior a hoy.', 'error');
      this.erroresFechas.fin = 'La fecha de finalización debe ser posterior a hoy.';
      errores.push(this.erroresFechas.fin);
      this.fechasValidas = false;
      //return false;
    }

    if (inicio.getTime() === fin.getTime()) {
      //swal.fire('Error', 'La fecha de inicio y la fecha de finalización no pueden ser iguales.', 'error');
      this.erroresFechas.fin = 'La fecha de inicio y la fecha de finalización no pueden ser iguales.';
      errores.push(this.erroresFechas.fin);
      this.fechasValidas = false;
      //return false;
    }

    if (fin <= inicio) {
      //swal.fire('Error', 'La fecha de finalización debe ser mayor que la fecha de inicio.', 'error');
       this.erroresFechas.fin = 'La fecha de finalización debe ser mayor que la fecha de inicio.';
       errores.push(this.erroresFechas.fin);
       this.fechasValidas = false;
       return false;
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

   public crearConvocatoria(form: any): void{


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

    if (!this.validarFechas()) {
        if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

    this.convocatoria.estado = 'ACTIVO';

    const convocatoriaToSend = {
      ...this.convocatoria,
      fechaInicio: this.convocatoria.fechaInicio
        ? moment(this.convocatoria.fechaInicio).format('YYYY-MM-DD')
        : null,
      fechaFin: this.convocatoria.fechaFin
        ? moment(this.convocatoria.fechaFin).format('YYYY-MM-DD')
        : null
    };

    this.convocatoriaService.createConvocatoria(convocatoriaToSend)
        .subscribe({
        next: (data: any) =>
        {
            console.log(data.convocatoria.fechaInicio);
            swal.fire('Nueva convocatoria',`Convocatoria: ${data.convocatoria.descripcion} creada con éxito!`, 'success');
            this.router.navigate(['/administrador/convocatoriasadministrador/convocatoriasadmin']);

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
            'Error al crear la convocatoria.'
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

  public actualizarConvocatoria(form: any):void{
    swal.fire({
              title: 'Está seguro?',
              text: `¿Seguro que desea actualizar la convocatoria: ${this.convocatoria.descripcion}?`,
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
                 if (!this.validarFechas()) {
                    return;
                  }
                  const convocatoriaToSend = {
                    ...this.convocatoria,
                    fechaInicio: this.convocatoria.fechaInicio
                      ? moment(this.convocatoria.fechaInicio).format('YYYY-MM-DD')
                      : null,
                    fechaFin: this.convocatoria.fechaFin
                      ? moment(this.convocatoria.fechaFin).format('YYYY-MM-DD')
                      : null
                  };

                  this.convocatoriaService.updateConvocatoria(convocatoriaToSend)
                      .subscribe({
                          next: (data: any) => { 
                            swal.fire('Convocatoria actualizada',`Convocatoria: ${data.convocatoria.descripcion} actualizada con éxito!`, 'success');
                            this.router.navigate(['/administrador/convocatoriasadministrador/convocatoriasadmin']);
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
                              'Error al actualizar la convocatoria.'
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
              `Actualización de la convocatoria: ${this.convocatoria.descripcion} cancelada`,
              'info'
              )
          }
        })
  }

  public cancelarConvocatoria(convocatoria :Convocatoria):void
  {

    if(!convocatoria.id)
    {
      swal.fire(
              'Creación cancelada!',
              `Creación de la convocatoria: ${convocatoria.descripcion} cancelada`,
              'info'
              )
    }
    else
    {
      swal.fire(
              'Actualización cancelada!',
              `Actualización de la convocatoria: ${convocatoria.descripcion} cancelada`,
              'info'
              )
    }
    this.router.navigate(['/administrador/convocatoriasadministrador/convocatoriasadmin']);
  }

  // 🔹 Utilidad para sumar 1 día
  private addOneDay(date: Date): Date {
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + 1);
    return newDate;
  }

  /*formatDate(date: Date | null): string {
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

  }*/

   onDateChangeFechaInicio(event: Date) {
    console.log('Fecha inicio seleccionada:', event);
    this.convocatoria.fechaInicio = event;
    if (event) {
      this.minDateFin = this.addOneDay(event);
    }
     console.log(this.validarFechas());
  }

  onDateChangeFechaFin(event: Date) {
    console.log('Fecha fin seleccionada:', event);
    this.convocatoria.fechaFin = event;
    console.log(this.validarFechas());
  }  

   logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
