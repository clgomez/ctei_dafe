import { Component, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model';
import { FileUpload } from 'primeng/fileupload';
import { ProyectoService } from '@app/Services/proyecto.service';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

import swal from 'sweetalert2';

@Component({
  selector: 'app-file-import',
  templateUrl: './file-import.component.html',
  styleUrls: ['./file-import.component.css'],
  
})

export class FileImportComponent {
  
  selectedFile: File | null = null;

  responseMessage: string | null = null;

  currentUser: User | null = null;

  public usuario: User | null;

  validationError = '';
  globalError = '';


  @ViewChild(FileUpload) fileUpload!: FileUpload;

  constructor(
    private proyectoService: ProyectoService, 
    private authService: AuthService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router
  ) { }

   ngOnInit() {

    this.authService.currentUser.subscribe(user => this.currentUser = user);
    console.log("username", this.currentUser.username);
    this.authService.getUserbyEmail(this.currentUser.username).subscribe
                    (user => {this.usuario = user});


  }
  

onSubmit(event: any): void {

  swal.fire({
    title: 'Está seguro?',
    text: `¿Seguro que desea importar el proyecto a la plataforma?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#007bff',
    confirmButtonText: 'Si, importar!',
    cancelButtonText: 'No, cancelar!',
    customClass: {
      confirmButton: 'btn btn-success',
      cancelButton: 'btn btn-danger',
    },
    buttonsStyling: false,
    reverseButtons: true
  } as any).then((result) => {
    if (result.value) {
      const files: File[] = event.files;

      if (files.length > 0 && this.usuario?.id) {
        const fileToUpload = files[0];

        swal.fire({
          title: 'Importando...',
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

            // Simulación de progreso
            const interval = setInterval(() => {
              progress += 10;
              if (progressBar && progressText) {
                progressBar.style.width = `${progress}%`;
                progressText.textContent = `${progress}%`;
              }

              if (progress >= 100) {
                clearInterval(interval);

                const extension = fileToUpload.name
                      .toLowerCase()
                      .split('.')
                      .pop();

                 let request$;

                if (extension === 'xlsx') {
                    request$ = this.proyectoService.importProyectoExcel(
                        fileToUpload,
                        this.usuario.id
                    );
                } else if (extension === 'pdf') {
                    request$ = this.proyectoService.importProyectoPdf(
                        fileToUpload,
                        this.usuario.id
                    );
                } else {
                    swal.fire(
                        'Archivo inválido',
                        'Solo se permiten archivos PDF o Excel.',
                        'error'
                    );
                    return;
                }     

                // Hacer upload cuando la barra llega a 100%
                request$.subscribe({
                  next: () => {
                    swal.fire({
                      title: 'Proyecto importado',
                      text: `Proyecto importado a la plataforma con éxito!`,
                      icon: 'success',
                      confirmButtonText: 'OK'
                    }).then((result) => {
                      if (result.isConfirmed) {
                        this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
                        this.fileUpload.clear();
                      }
                    });
                  },
                  error: (err) => {
                     // Cerrar el swal de progreso
                    swal.close();

                    console.error("Error en la importación:", err);

                    if (err.status === 0) {

                        this.fileUpload.clear();

                        swal.fire({
                            icon: 'warning',
                            title: 'El archivo fue modificado',
                            text: 'El archivo cambió después de haber sido seleccionado. Vuelva a seleccionarlo antes de importarlo nuevamente.'
                        });

                        return;
                    }

                    this.validationError = '';
                    this.globalError ='';

                    const backend = err.error;

                    console.log('mensajeGlobal');
                    console.log(err.mensajeGlobal);
                    
                    this.validationError = this.errorHandlerService.procesarError(
                      err,
                      null,
                      'Error al importar el proyecto.'
                    );

                    this.globalError = err.mensajeGlobal;

                    console.log('Mensaje final:');
                    console.log(this.validationError);


                    if (backend?.codigo === 'BUSINESS_ERROR') {

                        this.fileUpload.clear();

                    swal.fire({
                        icon: 'error',
                        title: 'Error al importar',
                        html: `
                            <ul style="text-align:left;padding-left:25px">
                                ${
                                  backend.mensajes
                                    .map((m: any) => `<li>${m.mensaje}</li>`)
                                    .join('')
                                }
                            </ul>
                        `
                    });

                    return;
                }

                 swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: this.validationError
                });
                   
                            
                  }
                });
              }
            }, 200); // velocidad de la animación (200ms por paso → 2s total)
          }
        });

      } else {
        swal.fire(
          'Importación cancelada!',
          `Importación del proyecto cancelada`,
          'info'
        )
      }
    }
  });
}

}

