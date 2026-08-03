import {Component, OnInit, OnDestroy, Input, ElementRef, HostListener } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Notificacion } from '@app/Models/notificacion.model';
import { NotificacionService } from '@app/Services/notificacion.service';
import { Usuario } from '@app/Models/usuario.model';
import { Subscription, interval } from 'rxjs';
import swal from 'sweetalert2';
import { EstadoNotificacion } from '@app/Enums/Notificaciones/estadonotificacion.enum';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {

  //@Input() usuario!: Usuario; // 👈 opcional si quieres pasarlo desde el topbar
  private _usuario!: Usuario;

  notificaciones: Notificacion[] = [];
  notificacionesVisible = false;
  unreadNotificationsCount = 0;

  notificacionSeleccionada: Notificacion | null = null;

  visibleDetalleNotificacion: boolean = false;

  private refreshSub!: Subscription;

  constructor(
    private notificacionService: NotificacionService,
    private elementRef: ElementRef,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {}

  @Input() set usuario(value: Usuario | null) {

    if (!value?.id) return;

    this._usuario = value;

    this.cargarNotificaciones();
    
    if (this.refreshSub) {
      this.refreshSub.unsubscribe();
    }
    // 🔁 refresco cada 10s SIN fugas de memoria
    this.refreshSub = interval(10000).subscribe(() => {
      this.cargarNotificaciones();
    });
}

  get usuario(): Usuario | null {
    return this._usuario ?? null;
  }

  ngOnDestroy(): void {
    if (this.refreshSub) {
      this.refreshSub.unsubscribe(); //  evita fuga de memoria
    }
  }

  // 🔔 Mostrar / ocultar menú
  toggleNotifications(): void {
    this.notificacionesVisible = !this.notificacionesVisible;
    
  }

  // 📥 Cargar notificaciones
  cargarNotificaciones(): void {
    if (!this.usuario?.id) return;
    this.notificacionService
      .getNotificacionesPorIdUsuario(this.usuario.id)
      .subscribe({
        next: (data) => {
          this.notificaciones = Array.isArray(data) ? data : [];

          // ordenar por fecha (más recientes primero)
          this.notificaciones.sort(
            (a, b) =>
              new Date(b.fechaNotificacion).getTime() -
              new Date(a.fechaNotificacion).getTime()
          );

          this.actualizarContador();
        },
        error: (err) => {
          console.error('Error cargando notificaciones', err);
        }
      });
  }

  // 🔢 Contador de no leídas
  actualizarContador(): void {
    this.unreadNotificationsCount = this.notificaciones.filter(n => !n.leida).length;
  }

  hasUnreadNotifications(): boolean {
    return this.unreadNotificationsCount > 0;
  }

  // ✅ Marcar como leída
  markAsRead(notificacion: Notificacion): void {
    if (notificacion.leida) return;

    notificacion.leida = true;
    notificacion.estado = EstadoNotificacion.LEIDA;

    this.notificacionService
      .updateNotificacionComoLeida(notificacion.id)
      .subscribe({
        next: (data) => {
          console.log(data.mensaje);
          this.actualizarContador();
        },
        error: (err) => {
          
          notificacion.leida = false; // rollback

          console.error(err);
          swal.fire({
            icon: 'error',
            title: 'Error',
            text:
              err.error?.mensajes?.[0]
              || 'Error al marcar como leida la notificación'
          });
        }
      });
  }

eliminarNotificacion(notificacion: Notificacion): void {

  swal.fire({
      title: 'Está seguro?',
      text: `¿Seguro que desea eliminar la notificación: ${notificacion.id}?`,
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
        
        this.notificacionService.deleteNotificacion(notificacion.id).subscribe({
        next: (data) => {
          this.notificaciones = this.notificaciones.filter(n => n.id !== notificacion.id);
          this.actualizarContador();
          this.cargarNotificaciones();

         swal.fire(
              'Notificacion Eliminada!',
              `Notificacion ${notificacion.id} eliminada con éxito.`,
              'success'
            );
        },
        error: (err) =>
        {
           console.error(err);
            swal.fire({
              icon: 'error',
              title: 'Error',
              text:
                err.error?.mensajes?.[0]
                || 'Error al eliminar la notificación'
            });
        }


      });
      }else
      {
        swal.fire(
              'Eliminación cancelada!',
              `Eliminación de la notificación: ${notificacion.id} cancelada`,
              'info'
        );

      }
    })

}


mostrarDetalleNotificacion(notificacion: Notificacion):void
{
    this.notificacionSeleccionada = notificacion;
    this.notificacionSeleccionada.fechaNotificacion =  notificacion.fechaNotificacion
          ? this.datePipe.transform(notificacion.fechaNotificacion, 'dd/MM/yyyy HH:mm:ss') || ''
          : '';
    this.visibleDetalleNotificacion = true;
}

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent): void {

    if (!this.notificacionesVisible) return;

    // 👇 evitar cerrar si hay un SweetAlert abierto
    if (document.querySelector('.swal2-container')) return;

    const clickedInside = this.elementRef.nativeElement.contains(event.target);

    if (!clickedInside) {
      this.notificacionesVisible = false;
    }
  }
}