import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { Rol } from '@app/Models/rol.model';
import { RolService } from '@app/Services/rol.service';
import { Usuario } from '@app/Models/usuario.model';
import { NgZone } from '@angular/core';
import { DatePipe } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';
import swal from 'sweetalert2';

class UsuarioSelectRol
{
    usuario:Usuario;
    //roles: Rol[];
    rolActual: Rol | null; 
    selectedRol: Rol;
    checked: boolean;
    seActualizoEstado: boolean;
    seActualizoRol: boolean;

}

@Component({
  selector: 'app-usuariosadmin',
  templateUrl: './usuariosadmin.component.html',
  styleUrls: ['./usuariosadmin.component.css']
})
export class UsuariosAdminComponent implements OnInit {

  currentUser: User | null = null;

  public visibleDetalleUsuario: boolean = false;

  public visibleCambiosVistaUsuarios: boolean = false;

  public usuario: Usuario;

  public usuarios: Usuario[] = [];

  public usuarioSeleccionarRol: UsuarioSelectRol;

  public usuariosSelectRol: UsuarioSelectRol[] = [];

  public usuariosSelectCambios: UsuarioSelectRol[] = [];

  //public usuarioSelectCambios: UsuarioSelectRol;

  public usuarioSeleccionado: Usuario | null = null;

  public identificacionUsuario: string = '';

  public rol: Rol = new Rol;

  public roles: Rol[] = [];

  public rolSeleccionado: Rol[] = [];

  public loading: boolean = true;

  //public first: number = 0;

  public selectedRole: Rol | null = null;

  public rolEscogido: Rol | null = null;

  public passwordCambiado: boolean = false;

  public fechaRegistroFormateada: string = '';

    
  constructor(
    private usuarioService: UsuarioService,
    private rolService: RolService,
    private authService: AuthService,
    private router: Router,
    private cd: ChangeDetectorRef,
    private ngZone: NgZone,
    private datePipe: DatePipe,
    private errorHandlerService: ErrorHandlerService
  ) { }

 
  minFechaNacimiento: string = '';
  maxFechaNacimiento: string = '';
  minAge = 9;
  validationError = '';
  globalError = '';
 
  ngOnInit() {

  const today = new Date();

  // Edad mínima (ya lo tienes)
  const minAgeDate = new Date(
    today.getFullYear() - this.minAge,
    today.getMonth(),
    today.getDate()
  );
  this.maxFechaNacimiento = minAgeDate.toISOString().split('T')[0];

  // Edad máxima (ej: 80 años)
  const maxAge = 80;
  const maxAgeDate = new Date(
    today.getFullYear() - maxAge,
    today.getMonth(),
    today.getDate()
  );
  this.minFechaNacimiento = maxAgeDate.toISOString().split('T')[0];

  this.rol.id = null;
  this.cargarRoles();
  this.cargarUsuarios();
  this.loading = false;

  }

  isFormatoFechaValido(): boolean {
  if (!this.usuarioSeleccionado.fechaNacimiento) return true;

  const formatoISO = /^\d{4}-\d{2}-\d{2}$/;

  if (!formatoISO.test(this.usuarioSeleccionado.fechaNacimiento)) {
    return false;
  }

  const [year, month, day] = this.usuarioSeleccionado.fechaNacimiento.split('-').map(Number);

  // Crear fecha SIN problemas de zona horaria
  const fecha = new Date(year, month - 1, day);

  return (
    fecha.getFullYear() === year &&
    fecha.getMonth() === month - 1 &&
    fecha.getDate() === day
  );
}

isMinAgeValid(): boolean {
  if (!this.usuarioSeleccionado.fechaNacimiento) return true;

  const [year, month, day] = this.usuarioSeleccionado.fechaNacimiento.split('-').map(Number);
  const inputDate = new Date(year, month - 1, day);

  const today = new Date();

  const minAgeDate = new Date(
    today.getFullYear() - this.minAge,
    today.getMonth(),
    today.getDate()
  );

  return inputDate <= minAgeDate;
}

  isMaxAgeValid(): boolean {
    if (!this.usuarioSeleccionado.fechaNacimiento) return true;

    const [year, month, day] = this.usuarioSeleccionado.fechaNacimiento.split('-').map(Number);
    const inputDate = new Date(year, month - 1, day);

    const minDate = new Date(this.minFechaNacimiento);

    return inputDate >= minDate;
  }

  validarCampos(): boolean {

  const errores: string[] = [];

  const nombrePattern = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$/;
  const usernamePattern = /^[A-Za-z0-9_]{4,20}$/;
  const emailPattern = /^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$/;
  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
  const telefonoPattern = /^\d{10}$/;
  const identificacionPattern = /^\d{6,15}$/;

  // Nombre
  if (!this.usuarioSeleccionado.nombre) {
    errores.push('El nombre es obligatorio');
  } else if (!nombrePattern.test(this.usuarioSeleccionado.nombre)) {
    errores.push('El nombre debe contener solo letras y tener entre 2 y 50 caracteres');
  }

  // Apellidos
  if (!this.usuarioSeleccionado.apellidos) {
    errores.push('Los apellidos son obligatorios');
  } else if (!nombrePattern.test(this.usuarioSeleccionado.apellidos)) {
    errores.push('Los apellidos deben contener solo letras y tener entre 2 y 50 caracteres');
  }

  // Username
  if (!this.usuarioSeleccionado.username) {
    errores.push('El nombre de usuario es obligatorio');
  } else if (!usernamePattern.test(this.usuarioSeleccionado.username)) {
    errores.push('El nombre de usuario debe tener entre 4 y 20 caracteres y solo letras, números o guión bajo _');
  }

  // Email
  if (!this.usuarioSeleccionado.email) {
    errores.push('El correo electrónico es obligatorio');
  } else if (!emailPattern.test(this.usuarioSeleccionado.email)) {
    errores.push('Formato de correo electrónico inválido. Ejemplo válido: usuario@dominio.com');
  }

  // Password
  if (!this.usuarioSeleccionado.password) {
    errores.push('La contraseña es obligatoria');
  } else if (!passwordPattern.test(this.usuarioSeleccionado.password)) {
    errores.push('La contraseña debe tener mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial');
  }

  // Teléfono
  if (!this.usuarioSeleccionado.telefono) {
    errores.push('El teléfono es obligatorio');
  } else if (!telefonoPattern.test(this.usuarioSeleccionado.telefono)) {
    errores.push('El teléfono debe contener exactamente 10 dígitos');
  }

  // Dirección
  if (!this.usuarioSeleccionado.direccion) {
    errores.push('La dirección es obligatoria');
  } else if (this.usuarioSeleccionado.direccion.length < 5) {
    errores.push('La dirección debe tener al menos 5 caracteres');
  }

  // Tipo identificación
  if (!this.usuarioSeleccionado.tipoIdentificacion) {
     errores.push('El tipo de identificación es obligatorio');
  }

  // Identificación
  if (!this.usuarioSeleccionado.identificacion) {
    errores.push('La identificación es obligatoria');
  } else if (!identificacionPattern.test(this.usuarioSeleccionado.identificacion)) {
    errores.push('La identificación debe contener entre 6 y 15 dígitos');
  }

  // Fecha nacimiento
  if (!this.usuarioSeleccionado.fechaNacimiento) {
    errores.push('La fecha de nacimiento es obligatoria');
  } else if (!this.isFormatoFechaValido()) {
    errores.push('Formato de fecha inválido (usa yyyy-MM-dd)');
  }else if (!this.isMinAgeValid()) {
    errores.push('Debes tener al menos 9 años para registrarte');
  } else if (!this.isMaxAgeValid()) {
    errores.push('La edad no puede ser mayor a 80 años para registrarse');
  }

  // Género
  if (!this.usuarioSeleccionado.genero) {
    errores.push('El género es obligatorio');
  }

  // Ocupación
  if (!this.usuarioSeleccionado.ocupacion) {
    errores.push('La ocupación es obligatoria');
  }

  // Mostrar todos los errores
  if (errores.length > 0) {
    console.log('Errores de validación:');
    errores.forEach(err => console.log('- ' + err));

    // Opcional: mostrar todos juntos en el HTML
    this.validationError = errores.join(' | ');
    return false;
  }

  return true;
}

onPasswordChange(): void {
  this.passwordCambiado = true;
}

  cargarRoles(): void
  {
    this.rolService.getRoles().subscribe({
        next: (roles) =>
        {
            console.log(roles);
            this.roles = roles;
            console.log(roles.length);
          if(this.roles.length == 0)
             //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
            console.log('lista vacia');

        },
        //error: err => {
          //console.log(err.error.mensaje)
          //swal.fire("error al consultar roles en la bd", err.error.mensaje,"error");
        //}
        });

  }

  cargarUsuarios(): void
  {
    this.usuarioService.getUsuarios().subscribe({
        next: (usuarios) =>
        { this.usuarios =
            Array.isArray(usuarios)
              ? usuarios
              : [];

          this.cargarUsuariosSelectRol();

        },
         error: (err) => {
                  console.error(err);
                  swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text:
                      err.error?.mensajes?.[0]
                      || 'Error al consultar usuarios'
                  });
          }
        });
  }

  cargarUsuariosSelectRol(): void {

  const usuariosSelectRolTemp: UsuarioSelectRol[] = [];

  this.usuarios.forEach(usuario => {

    const usrSelRol = new UsuarioSelectRol();

    // Usuario base
    usrSelRol.usuario = usuario;

    // Estado (switch)
    usrSelRol.checked = usuario.estado === 'ACTIVO';

    // Rol actual SEGURO (sin romper)
    if (usuario.roles && usuario.roles.length > 0) {
      usrSelRol.rolActual = usuario.roles[0];
    } else {
      usrSelRol.rolActual = null; // 
    }

    // Rol seleccionado (para cambio)
    usrSelRol.selectedRol = new Rol();
    usrSelRol.selectedRol.id = null;
    usrSelRol.selectedRol.rolNombre = null;

    // Flags de control
    usrSelRol.seActualizoEstado = false;
    usrSelRol.seActualizoRol = false;

    usuariosSelectRolTemp.push(usrSelRol);
  });

  // Reemplazo inmutable (mejor práctica Angular)
  this.usuariosSelectRol = [...usuariosSelectRolTemp];

  console.log('usuariosSelectRol:', this.usuariosSelectRol);
}
   

  actualizarCambiosDeUsuarios():void
  {

    this.usuariosSelectCambios = [];

    let usuarioSelectCambios: UsuarioSelectRol = null;

    let rol: Rol = null;

    let existenCambios: boolean = false;


    this.usuariosSelectRol.forEach(usuarioSelectRol => {

       console.log('id usuario:',usuarioSelectRol.usuario.id);
       console.log('estado:',usuarioSelectRol.usuario.estado);
       console.log('checked:',usuarioSelectRol.checked);
       console.log('se actualizo estado:',usuarioSelectRol.seActualizoEstado);
       console.log('se actualizo estado:',usuarioSelectRol.seActualizoRol);

       usuarioSelectCambios = new UsuarioSelectRol();

       usuarioSelectCambios.usuario = new Usuario();

       usuarioSelectCambios.usuario.id = usuarioSelectRol.usuario.id;
       usuarioSelectCambios.usuario.tipoIdentificacion = usuarioSelectRol.usuario.tipoIdentificacion;
       usuarioSelectCambios.usuario.identificacion = usuarioSelectRol.usuario.identificacion;
       usuarioSelectCambios.usuario.nombre = usuarioSelectRol.usuario.nombre;
       usuarioSelectCambios.usuario.apellidos = usuarioSelectRol.usuario.apellidos;
       usuarioSelectCambios.usuario.direccion = usuarioSelectRol.usuario.direccion;
       usuarioSelectCambios.usuario.telefono = usuarioSelectRol.usuario.telefono;
       usuarioSelectCambios.usuario.fechaNacimiento = usuarioSelectRol.usuario.fechaNacimiento;
       usuarioSelectCambios.usuario.genero = usuarioSelectRol.usuario.genero;
       usuarioSelectCambios.usuario.ocupacion = usuarioSelectRol.usuario.ocupacion;
       usuarioSelectCambios.usuario.email = usuarioSelectRol.usuario.email;
       usuarioSelectCambios.usuario.username = usuarioSelectRol.usuario.username;
       usuarioSelectCambios.usuario.fechaRegistro = usuarioSelectRol.usuario.fechaRegistro;
       //usuarioSelectCambios.usuario.estado = usuarioSelectRol.usuario.estado;

       rol = new Rol();
       rol.id = null;
       rol.rolNombre = null;

       usuarioSelectCambios.usuario.roles = new Array();
       usuarioSelectCambios.usuario.roles.push(rol);
       usuarioSelectCambios.usuario.roles[0].id = null;
       usuarioSelectCambios.usuario.roles[0].rolNombre = null;
       usuarioSelectCambios.seActualizoRol = false;
       usuarioSelectCambios.seActualizoEstado = false;

       if(usuarioSelectRol.usuario.estado == 'ACTIVO')
       {
           if(usuarioSelectRol.checked == false)
           {
             usuarioSelectCambios.usuario.estado = 'NO ACTIVO';
             usuarioSelectCambios.seActualizoEstado = true;
             console.log('se actualizo estado en activo:',usuarioSelectCambios.seActualizoEstado);
           }
       }
       if(usuarioSelectRol.usuario.estado == 'NO ACTIVO')
       {
           if(usuarioSelectRol.checked == true)
           {
             usuarioSelectCambios.usuario.estado = 'ACTIVO';
             usuarioSelectCambios.seActualizoEstado = true;
             console.log('se actualizo estado en no activo:',usuarioSelectCambios.seActualizoEstado);
           }
       }

       this.rol = this.roles.find(rol => rol.id == usuarioSelectRol.selectedRol.id)

       if(this.rol != null)
       {
           console.log('rol final', this.rol);
           if(usuarioSelectRol.usuario.roles?.[0]?.rolNombre != this.rol.rolNombre)
            {
           usuarioSelectCambios.usuario.roles[0].id = this.rol.id;
           usuarioSelectCambios.usuario.roles[0].rolNombre = this.rol.rolNombre;
           usuarioSelectCambios.seActualizoRol = true;
           }

       }


       if(usuarioSelectCambios.seActualizoEstado == true || usuarioSelectCambios.seActualizoRol == true)
       {
          this.usuariosSelectCambios.push(usuarioSelectCambios);
           usuarioSelectRol.seActualizoEstado = false;
           usuarioSelectRol.seActualizoRol = false;
           existenCambios = true;

       }

   });

   if(existenCambios)
     this.visibleCambiosVistaUsuarios = true;
   else swal.fire("No existen cambios en la lista de usuarios");

  }


confirmarCambiosVistaUsuarios(form: any): void {

  this.visibleCambiosVistaUsuarios = false;

  swal.fire({
    title: '¿Está seguro?',
    text: `¿Confirma realizar cambios en estos usuarios?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, confirmar',
    cancelButtonText: 'Cancelar',
    reverseButtons: true
  } as any).then((result) => {

    if (!result.value) {
      swal.fire(
        'Cancelado',
        'Actualización de cambios cancelada',
        'info'
      );
      return;
    }

    this.globalError = '';
    this.validationError = '';

    const erroresGlobales: string[] = [];
    let totalExitos = 0;

    // 🔥 Construcción de requests
    const requests = this.usuariosSelectCambios.map(usuarioSelectCambios => {

      const usuarioSelectRol = this.usuariosSelectRol.find(
        i => i.usuario.id === usuarioSelectCambios.usuario.id
      );

      if (!usuarioSelectRol) return of(null);

      // ✅ Aplicar cambios locales
      if (usuarioSelectCambios.usuario.estado != null) {
        usuarioSelectRol.usuario.estado = usuarioSelectCambios.usuario.estado;
      }

      if (usuarioSelectCambios.usuario.roles[0]?.id != null) {
        usuarioSelectRol.usuario.roles[0].id = usuarioSelectCambios.usuario.roles[0].id;
        usuarioSelectRol.usuario.roles[0].rolNombre = usuarioSelectCambios.usuario.roles[0].rolNombre;
      }

      // ✅ Preparar objeto
      const usuarioEnviar: any = { ...usuarioSelectRol.usuario };
      delete usuarioEnviar.password;

      // ✅ Formato roles correcto
      if (usuarioSelectRol.usuario.roles?.length > 0) {
        usuarioEnviar.roles = [
          usuarioSelectRol.usuario.roles[0].rolNombre
        ];
      } else {
        delete usuarioEnviar.roles;
      }

      // 🚀 Request
      return this.usuarioService.updateUsuario(usuarioEnviar).pipe(

        map(() => {
          totalExitos++;
          return { ok: true, id: usuarioEnviar.id };
        }),

        catchError((error) => {

          console.log(`Error usuario ${usuarioEnviar.id}`, error);

          this.validationError = '';
          this.globalError = '';
  
          console.log('mensajeGlobal');
          console.log(error.mensajeGlobal);
        
          this.validationError = this.errorHandlerService.procesarError(
            error,
            form,
            'Error al actualizar usuarios.'
          );

          this.globalError = error.mensajeGlobal;

          console.log('Mensaje final:');
          console.log(this.validationError);
    
          // limpiar mensaje global
          if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 5000);
            }

          if (this.globalError) {
            setTimeout(() => {
              this.globalError = '';
            }, 5000);
          }

          // Guardar error acumulado
          erroresGlobales.push(`ID ${usuarioEnviar.id}: ${this.validationError}`);

          return of({ ok: false, id: usuarioEnviar.id });
        })
      );
    });

    // 🚀 Ejecutar todos
    forkJoin(requests).subscribe({

      next: () => {

        this.cargarUsuarios();

        this.ngZone.run(() => {
          this.cd.detectChanges();
        });

        // 🔥 RESULTADO FINAL PRO
        if (erroresGlobales.length > 0) {

          swal.fire({
            title: 'Proceso completado con errores',
            html: `
              <b>Usuarios actualizados correctamente:</b> ${totalExitos} <br><br>
              <b>Errores:</b><br>
              <pre style="text-align:left; max-height:300px; overflow:auto;">${erroresGlobales.join('\n\n')}</pre>
            `,
            icon: 'warning',
            width: 600
          });

        } else {

          swal.fire(
            'Éxito',
            `Todos los usuarios (${totalExitos}) fueron actualizados correctamente`,
            'success'
          );

        }
      },

      error: (error) => {
        // Muy raro que caiga aquí
        console.log('Error global:', error);

        swal.fire(
          'Error crítico',
          'Ocurrió un error general en la actualización',
          'error'
        );
      }
    });

  });

}


  mostrarDetalleUsuario(usuario: Usuario, rol: Rol):void
  {

   console.log('usuario id hola', usuario.id)
   console.log('rol del usuario', rol);
   this.usuarioSeleccionado = usuario;

  if (!this.usuarioSeleccionado.roles || this.usuarioSeleccionado.roles.length === 0) {
    this.usuarioSeleccionado.roles = [new Rol()];
    this.usuarioSeleccionado.roles[0].rolNombre = 'Sin Rol';
    this.usuarioSeleccionado.roles[0].id = null;
  }

   // FORMATEAR FECHA AQUÍ
  if (usuario.fechaRegistro) {
    this.fechaRegistroFormateada = this.datePipe.transform(
      usuario.fechaRegistro,
      'dd/MM/yyyy HH:mm:ss'
    ) || '';
  }

   this.selectedRole = rol; 
   this.passwordCambiado = false; // importante
   this.visibleDetalleUsuario = true;
  }
  
   actualizarUsuario(form: any): void {

  // ✅ 1. Validar formulario antes de cualquier cosa
  if (form.invalid) {
    Object.values(form.controls).forEach((control: any) => {
      //control.markAsTouched();
    });
    //return;
  }

   // Validar campos obligatorios
      if (!this.validarCampos()) {
       if (this.validationError) {
              setTimeout(() => {
                this.validationError = '';
              }, 4000);
            }
      return;
    }

   this.visibleDetalleUsuario = false;

  
  // ✅ 2. Confirmación SOLO si el formulario es válido
  swal.fire({
    title: '¿Está seguro?',
    text: `¿Seguro que desea actualizar el usuario: ${this.usuarioSeleccionado.nombre} ${this.usuarioSeleccionado.apellidos}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, actualizar',
    cancelButtonText: 'Cancelar',
    reverseButtons: true
  } as any).then((result) => {

    if (!result.value) {
      swal.fire(
        'Cancelado',
        'La actualización fue cancelada',
        'info'
      );
      return;
    }

    // ✅ 3. Preparar objeto a enviar
    const usuarioEnviar: any = { ...this.usuarioSeleccionado };

    // limpiar errores previos
    this.globalError = '';
    this.validationError = '';

    // ✅ Password solo si cambió
    if (!this.passwordCambiado) {
      delete usuarioEnviar.password;
    }

    // ✅ Manejo correcto de roles (string[])
    if (this.selectedRole?.id) {
      const rolEscogido = this.roles.find(r => r.id == this.selectedRole.id);
      if (rolEscogido) {
          usuarioEnviar.roles = [rolEscogido.rolNombre];
      }
    } else {
      delete usuarioEnviar.roles;
    }

    // ✅ 4. Llamado al backend
    this.usuarioService.updateUsuario(usuarioEnviar).subscribe({

      next: (data) => {

        this.passwordCambiado = false;

        // actualizar tabla local
        const index = this.usuariosSelectRol.findIndex(
          i => i.usuario.id === this.usuarioSeleccionado.id
        );

        if (index !== -1) {
          this.usuariosSelectRol[index].selectedRol.id = null;

          this.usuariosSelectRol[index].checked =
            this.usuarioSeleccionado.estado === 'ACTIVO';
        }

        this.cargarUsuarios();

        this.ngZone.run(() => {
          this.cd.detectChanges();
        });

        // ✅ cerrar modal SOLO aquí
        this.visibleDetalleUsuario = false;

        swal.fire(
          'Actualizado',
          `Usuario ${this.usuarioSeleccionado.nombre} actualizado correctamente`,
          'success'
        );
      },

      error: (error) => {

        console.log('Error completo:', error);
  
          this.validationError = '';
          this.globalError = '';
  
          this.visibleDetalleUsuario = true;

          console.log('mensajeGlobal');
          console.log(error.mensajeGlobal);
        
          this.validationError = this.errorHandlerService.procesarError(
            error,
            form,
            'Error al actualizar el usuario.'
          );

          this.globalError = error.mensajeGlobal;

          console.log('Mensaje final:');
          console.log(this.validationError);
    
        // limpiar mensaje global
       if (this.validationError) {
          setTimeout(() => {
            this.validationError = '';
          }, 5000);
        }

      if (this.globalError) {
        setTimeout(() => {
          this.globalError = '';
        }, 5000);
      }
        
    }

    });

  });

}

  eliminarUsuario(usuario: Usuario): void {
      swal.fire({
        title: 'Está seguro?',
        text: `¿Seguro que desea eliminar el usuario: ${usuario.nombre} ${usuario.apellidos}?`,
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
          this.usuarioService.deleteUsuario(usuario.id).subscribe({
              next: (data) => {
                  console.log('usuario eliminado: ',usuario.id);
                  this.usuariosSelectRol = this.usuariosSelectRol.filter(usuSelRol => usuSelRol.usuario.id !== usuario.id);

                  this.cargarUsuarios();

                  swal.fire(
                    'Usuario Eliminado!',
                    `Usuario: ${usuario.nombre} ${usuario.apellidos} eliminado con éxito.`,
                    'success'
                  )
                },
                error: (err) => {
                  console.error(err);
                  swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text:
                      err.error?.mensajes?.[0]
                      || 'Error al eliminar cuenta de usuario'
                  });
                }
          });
  
        }else
        {
          swal.fire(
                'Eliminación cancelada!',
                `Eliminación del usuario: ${usuario.nombre} ${usuario.apellidos} cancelada`,
                'info'
          )
  
        }
      })
    }

  onSwitchChange(event: any) {
    console.log('Evento:', event.checked);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
