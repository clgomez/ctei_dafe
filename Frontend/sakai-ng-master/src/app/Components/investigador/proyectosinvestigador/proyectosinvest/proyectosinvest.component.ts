import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import { MenuItem } from 'primeng/api';
import swal from 'sweetalert2';


@Component({
  selector: 'app-proyectosinvest',
  templateUrl: './proyectosinvest.component.html',
  styleUrls: ['./proyectosinvest.component.css']
})
export class ProyectosInvestComponent implements OnInit{
  currentUser: User | null = null;

  public usuario: User | null;

  public visibleDetalleProyecto: boolean = false;

  public proyectos: Proyecto[];

  proyectoSeleccionado: Proyecto | null = null;

  public loading: boolean = true;

  data: any =[];

  validationError = '';
  globalError = '';

  //itemsExportar: MenuItem[];

  constructor(
    private proyectoService: ProyectoService,
    private authService: AuthService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private datePipe: DatePipe
  ) { }

  ngOnInit() {

    /*this.itemsExportar = [
    {
      label: 'Exportar a PDF',
      icon: 'pi pi-file-pdf',
      command: () => {
        this.exportarProyecto(this.proyectoSeleccionado, 'PDF');
      }
    },
    {
      label: 'Exportar a Excel',
      icon: 'pi pi-file-excel',
      command: () => {
        this.exportarProyecto(this.proyectoSeleccionado, 'Excel');
      }
    }
  ];*/


    this.authService.currentUser.subscribe(user => this.currentUser = user);

    console.log("username", this.currentUser.username);
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
                    (user => {this.usuario = user, this.cargarProyectosPorIdUsuario()});

    this.loading = false;

  }

getItemsExportar(proyecto: Proyecto): MenuItem[] {
  return [
    {
      label: 'Exportar a PDF',
      icon: 'pi pi-file-pdf',
      command: ($event) => {
                            $event.originalEvent.preventDefault();
                            this.onMenuItemClick(proyecto, 'PDF')
                           }
    },
    {
      label: 'Exportar a Excel',
      icon: 'pi pi-file-excel',
      command: ($event) =>{ 
                           $event.originalEvent.preventDefault(); 
                           this.onMenuItemClick(proyecto, 'Excel')
                          } 
    }
  ];
}

 /* cargarProyectosPorIdUsuario():void
  {
    this.proyectoService.getProyectosPorUsuario(this.usuario.id).subscribe({
        next: (respose) =>
        { this.proyectos =  Array.isArray(respose) ? respose : [];
            console.log(respose.length);
            
          if(this.proyectos.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar proyectos en la bd", err.error.mensaje,"error");
        }
        });

  }*/

 cargarProyectosPorIdUsuario(): void {

  this.proyectoService
      .getProyectosPorIdUsuario(this.usuario.id).subscribe({

        next: (response) => {

          const proyectosArr = Array.isArray(response)
            ? response
            : [];

          this.proyectos = proyectosArr.map(p => {

            p.fechaCreacion = p.fechaCreacion
              ? this.datePipe.transform(
                  p.fechaCreacion,
                  'dd/MM/yyyy HH:mm:ss'
                ) || ''
              : '';
            
            p.fechaActualizacion = p.fechaActualizacion
              ? this.datePipe.transform(
                  p.fechaActualizacion,
                  'dd/MM/yyyy HH:mm:ss'
                ) || ''
              : '';

            return {
              ...p,
              itemsExportar: this.getItemsExportar(p)
            };
          });
        },

        error: (err) => {

          console.error(err);

          swal.fire({
            icon: 'error',
            title: 'Error',
            text:
              err.error?.mensajes?.[0]
              || 'Error al consultar proyectos'
          });
        }
      });
}

  eliminarProyecto(proyecto: Proyecto): void {
    swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea eliminar el proyecto: ${proyecto.titulo}?`,
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

       this.proyectoService.deleteProyecto(proyecto.id)
        .subscribe({

          next: (data) => {

            this.proyectos =
              this.proyectos.filter(p => p !== proyecto);

            swal.fire(
              'Proyecto Eliminado!',
              `Proyecto: ${proyecto.titulo} ${data.mensaje} `,
              'success'
            );
          },

          error: (err) => {

            console.error(err);

            swal.fire({
              icon: 'error',
              title: 'Error',
              text:
                err.error?.mensajes?.[0]
                || 'No fue posible eliminar el proyecto'
            });
          }
        });

      }else
      {
        swal.fire(
              'Eliminación cancelada!',
              `Eliminación del proyecto: ${proyecto.titulo} cancelada`,
              'info'
        )

      }
    })
  }


mostrarDetalleProyecto(proyecto: Proyecto):void
   {
    this.proyectoSeleccionado = proyecto;
    this.visibleDetalleProyecto = true;
   }

setAriaExpanded(proyectoId: number, expanded: boolean) {
  const btn = document.getElementById(`btn-export-${proyectoId}`);
  if (btn) {
    btn.setAttribute('aria-expanded', expanded ? 'true' : 'false');
  }
}

onMenuItemClick(proyecto: Proyecto, formato: string) {
  this.onMenuClose(proyecto.id);
  setTimeout(() => this.exportarProyecto(proyecto, formato), 0);
}

onMenuClose(proyectoId: number) {
  const safe = document.getElementById(`btn-export-${proyectoId}`) as HTMLElement | null;
  if (safe) {
    safe.focus();
  }
}

private descargar(blob: Blob, ext: string) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');

  a.href = url;
  a.download = `proyecto.${ext}`;
  a.click();

  window.URL.revokeObjectURL(url);
}

 
exportarProyecto(proyecto: Proyecto, formatoArchivo: string): void {

  if (!proyecto) {
    swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No hay proyecto seleccionado para exportar.'
    });
    return;
  }

  this.proyectoSeleccionado = proyecto;

  swal.fire({
    title: '¿Está seguro?',
    text: `¿Seguro que desea exportar el proyecto: ${proyecto.titulo} en formato ${formatoArchivo}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, exportar',
    cancelButtonText: 'Cancelar',
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    customClass: {
      confirmButton: 'btn btn-success',
      cancelButton: 'btn btn-danger'
    },
    buttonsStyling: false,
    reverseButtons: true

  }).then((result) => {

    if (!result.isConfirmed) {

      swal.fire({
        icon: 'info',
        title: 'Exportación cancelada',
        text: `La exportación del proyecto "${proyecto.titulo}" fue cancelada.`
      });

      return;
    }

    // Ventana de carga
    swal.fire({
      title: 'Exportando...',
      html: 'Por favor espere mientras se genera el archivo.',
      allowOutsideClick: false,
      allowEscapeKey: false,
      didOpen: () => {
        swal.showLoading();
      }
    });


   /*swal.fire({
    title: 'Exportando...',
    html: `
    <div style="width: 100%; background: #eee; border-radius: 10px; height: 20px;">
    <div id="progress-bar" style="width: 0%; height: 100%; background: #3085d6; border-radius: 10px;"></div>
    </div>
    <p id="progress-text" style="margin-top: 10px; font-weight: bold;">0%</p>
    `,
    allowOutsideClick: false,
    showConfirmButton: false,
    didOpen: () => {
    let progress = 0;
    const progressBar = document.getElementById('progress-bar') as HTMLElement;
    const progressText = document.getElementById('progress-text') as HTMLElement;

    // Simulación de progreso (ejemplo cada 200ms sube 10%)
    const interval = setInterval(() => {
    progress += 10;
    if (progressBar && progressText) {
    progressBar.style.width = `${progress}%`;
    progressText.textContent = `${progress}%`;
    }

    if (progress >= 100) {
    clearInterval(interval);

    // Cuando la barra llega al 100% mostramos el modal de éxito

    }
    }, 200); // velocidad de progreso (200ms por paso → 2s total)
    }
  });
  */

    if (formatoArchivo === 'PDF') {

      this.proyectoService.exportProyectoToPdf(proyecto.id)
        .subscribe({

          next: (blob: Blob) => {

            swal.close();

            this.descargar(blob, 'pdf');

            swal.fire({
              icon: 'success',
              title: 'Proyecto exportado',
              text: `El proyecto "${proyecto.titulo}" fue exportado correctamente a PDF.`
            });

          },

          error: (err) => {

            swal.close();

            this.manejarErrorExportacion(err);

          }

        });

    } else {

      this.proyectoService.exportProyectoToExcel(proyecto.id)
        .subscribe({

          next: (blob: Blob) => {

            swal.close();

            this.descargar(blob, 'xlsx');

            swal.fire({
              icon: 'success',
              title: 'Proyecto exportado',
              text: `El proyecto "${proyecto.titulo}" fue exportado correctamente a Excel.`
            });

          },

          error: (err) => {

            swal.close();

            this.manejarErrorExportacion(err);

          }

        });

    }

  });

}


private manejarErrorExportacion(err: any): void {

  console.error(err);

  if (err.error instanceof Blob) {

    const reader = new FileReader();

    reader.onload = () => {

      try {

        const backendError = JSON.parse(reader.result as string);

        console.log(backendError);

        // Ahora sí el ErrorHandler puede entenderlo
        const mensaje = this.errorHandlerService.procesarError(
          { error: backendError },
          null,
          'Error al exportar el proyecto.'
        );

        swal.fire({
          icon: 'error',
          title: 'Error',
          text: mensaje
        });

      } catch {

        swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Ocurrió un error inesperado.'
        });

      }

    };

    reader.readAsText(err.error);

    return;
  }

  const mensaje = this.errorHandlerService.procesarError(
    err,
    null,
    'Error al exportar el proyecto.'
  );

  swal.fire({
    icon: 'error',
    title: 'Error',
    text: mensaje
  });

}


onKeyDown(event: KeyboardEvent, proyecto: Proyecto) {
  const btn = document.getElementById(`btn-export-${proyecto.id}`) as HTMLElement;
  
  if (!btn) return;

  switch (event.key) {
    case 'Enter':
    case ' ':
      event.preventDefault();
      btn.click();
      break;
    case 'Escape':
      event.preventDefault();
      this.onMenuClose(proyecto.id);
      break;
  }
}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
