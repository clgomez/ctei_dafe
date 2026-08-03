import { Component, Input, OnInit, ChangeDetectorRef, ElementRef, HostListener } from '@angular/core';
import { Usuario } from '@app/Models/usuario.model';
import { UsuarioService } from '@app/Services/usuario.service';
import { Rol } from '@app/Models/rol.model';
import { RolService } from '@app/Services/rol.service';
import { AuthService } from '@app/Services/auth.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';
import swal from 'sweetalert2';
import { ErrorHandlerService } from '@app/Services/errorhandler.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  @Input() usuario!: Usuario;

  profileMenuVisible = false;

  visiblePerfilDelUsuario = false;
  visibleConfiguracionCuenta = false;

  usuarioSeleccionado: Usuario | null = null;
  fechaRegistroFormateada = '';

  passwordCambiado = false;
  validationError = '';
  globalError = '';
  minFechaNacimiento: string = '';
  maxFechaNacimiento: string = '';
  minAge = 9;
   
  selectedRole: Rol | null = null;
  rol: Rol = new Rol;
  roles: Rol[] = [];

  constructor(
    private usuarioService: UsuarioService,
    private rolService: RolService,
    private authService: AuthService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private cd: ChangeDetectorRef,
    private datePipe: DatePipe,
    private elementRef: ElementRef,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void 
  {
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

  // 👇 Iniciales
  getUserInitials(): string {
    if (!this.usuario) return '';

    const n = this.usuario.nombre?.trim() || '';
    const a = this.usuario.apellidos?.trim() || '';

    if (n && a) return (n[0] + a[0]).toUpperCase();
    if (n.length >= 2) return n.substring(0, 2).toUpperCase();
    if (a.length >= 2) return a.substring(0, 2).toUpperCase();

    return (n[0] || a[0] || '').toUpperCase();
  }

  // 👇 Toggle menú
  toggleProfileMenu(): void {
    this.profileMenuVisible = !this.profileMenuVisible;
  }


  cargarUsuarioPorId(id: number, callback?: (usuario: Usuario) => void): void {

    this.usuarioService.getUsuarioPorId(id).subscribe({
      next: (usuario) => {

         if (!usuario || !usuario.id) {
          this.visiblePerfilDelUsuario = false;
          this.usuarioSeleccionado = null;

          swal.fire(
            'Usuario no encontrado',
            'El usuario solicitado no existe.',
            'warning'
          );
          return;
        }

        this.usuario = usuario;

        if (callback) {
          callback(usuario);
        }
      },
      error: (error) => {
          console.log('Error completo:', error);
  
          this.validationError = '';
          this.globalError = '';
  
          this.visiblePerfilDelUsuario = false;
          this.usuarioSeleccionado = null;

          console.log('mensajeGlobal');
          console.log(error.mensajeGlobal);
        
          this.validationError = this.errorHandlerService.procesarError(
            error,
            null,
            'Error al consultar el usuario.'
          );

          this.globalError = error.mensajeGlobal;

         console.log('Mensaje final:');
         console.log(this.validationError);

          swal.fire(
            'Error',
            `${this.globalError} ${this.validationError}`,
            'error'
          );
      }
    });
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

  // 👇 Cargar perfil
  mostrarPerfilDelUsuario(): void {
    //  Validación inicial
    if (!this.usuario?.id) {
      console.warn('Usuario aún no cargado');
      return;
    }

    console.log('usuario id', this.usuario.id);

    // CARGAR DATOS FRESCOS DESDE BACKEND
    this.cargarUsuarioPorId(this.usuario.id, (usuarioActualizado) => {

      // CLONAR (evita mutaciones raras)
    this.usuarioSeleccionado = {
        ...usuarioActualizado,
        roles: usuarioActualizado.roles?.length ? usuarioActualizado.roles : [{ rolNombre: 'Sin Rol' }]
      };

    // FORMATEAR FECHA
    if (usuarioActualizado.fechaRegistro) {
      this.fechaRegistroFormateada = this.datePipe.transform(
        usuarioActualizado.fechaRegistro,
        'dd/MM/yyyy HH:mm:ss'
      ) || '';
    } else {
      this.fechaRegistroFormateada = '';
    }

    // RESET DE ESTADOS
    this.selectedRole = new Rol();
    this.selectedRole.id = null;

    this.passwordCambiado = false;

    // ABRIR MODAL SOLO CUANDO YA TIENES DATOS
    this.visiblePerfilDelUsuario = true;

  });

}

  // 👇 Actualizar
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

      this.visiblePerfilDelUsuario = false;

    // ✅ 2. Confirmación SOLO si el formulario es válido
    swal.fire({
      title: '¿Está seguro?',
      text: `¿Seguro que desea actualizar el usuario: ${this.usuario.nombre} ${this.usuario.apellidos}?`,
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
      this.validationError = '';
      this.globalError = '';
  
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
  
        next: (data: any) => {
  
          this.passwordCambiado = false;
   
          this.cargarUsuarioPorId(this.usuario.id);
  
          this.ngZone.run(() => {
            this.cd.detectChanges();
          });
  
          // ✅ cerrar modal SOLO aquí
          this.visiblePerfilDelUsuario = false;
  
          swal.fire(
            'Actualizado',
            `Usuario ${data.usuario.nombre} ${data.usuario.apellidos} actualizado correctamente`,
            'success'
          );
        },
  
        error: (error) => {
  
          console.log('Error completo:', error);
  
          this.validationError = '';
          this.globalError = '';
  
          this.visiblePerfilDelUsuario = true;

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

  // 👇 Logout
  logout(): void {
    this.authService.logout();
    this.profileMenuVisible = false;
    this.router.navigate(['/login']);
  }

  // 👇 Configuración
  abrirConfiguracionCuenta(): void {
    this.visibleConfiguracionCuenta = true;
  }

  // 👇 Eliminar cuenta
   eliminarCuenta(): void {

    if (this.esAdministrador()) {
    swal.fire(
      'Acción no permitida',
      'Un administrador no puede eliminar su propia cuenta.',
      'warning'
    );
    return;
  }

    this.visibleConfiguracionCuenta = false;

      swal.fire({
        title: 'Está seguro?',
        text: `¿Seguro que desea eliminar la cuenta de usuario: ${this.usuario.email}?`,
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
          // eliminando usuario, roles en cascada
         this.usuarioService.deleteUsuario(this.usuario.id).subscribe({
                  next: (data: any) => {
                           
                    //console.log('usuario eliminado: ',this.usuario.id);
                    this.authService.logout();
                    this.router.navigate(['/login']);
                    swal.fire(
                                  'Cuenta de Usuario Eliminada!',
                                  `Usuario: ${this.usuario.email} ${data.mensaje}.`,
                                  'success'
                              )
    
                  },
                  error: (error) => {
                    //console.error(`Error al eliminar cuenta de usuario`, err);

                      console.log('Error completo:', error);
              
                      this.validationError = '';
                      this.globalError = '';
              
                      this.visiblePerfilDelUsuario = false;
                      this.usuarioSeleccionado = null;

                      console.log('mensajeGlobal');
                      console.log(error.mensajeGlobal);
                    
                      this.validationError = this.errorHandlerService.procesarError(
                        error,
                        null,
                        'Error al eliminar cuenta de usuario'
                      );

                      this.globalError = error.mensajeGlobal;

                    console.log('Mensaje final:');
                    console.log(this.validationError);

                      swal.fire(
                        'Error',
                        `${this.globalError} ${this.validationError}`,
                        'error'
                      );             

                  }
                });    
  
        }else
        {
          swal.fire(
                'Eliminación cancelada!',
                `Eliminación de la cuenta de usuario: ${this.usuario.email} cancelada`,
                'info'
          )
  
        }
      })
    }

  onPasswordChange(): void {
    this.passwordCambiado = true;
  }

   esAdministrador(): boolean {
    return this.usuario?.roles?.some(r => r.rolNombre === 'ROL_ADMINISTRADOR');
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent): void {

    const target = event.target as HTMLElement;

    // ignorar clicks dentro de dialog
    if (target.closest('.p-dialog')) return;

    const clickedInside = this.elementRef.nativeElement.contains(target);

    if (!clickedInside) {
        this.profileMenuVisible = false; // o notificacionesVisible
    }
  }

}