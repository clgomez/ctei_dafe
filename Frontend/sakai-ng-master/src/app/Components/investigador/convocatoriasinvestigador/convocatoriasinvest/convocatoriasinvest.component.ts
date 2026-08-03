import { Component, OnInit } from '@angular/core';
import { Convocatoria } from '@app/Models/convocatoria.model';
import { ConvocatoriaService } from '@app/Services/convocatoria.service';
import { Router } from '@angular/router';
import { AuthService } from '@app/Services/auth.service'; 
import { User } from '@app/Models/user.model'; 
import { DatePipe } from '@angular/common';
import swal from 'sweetalert2';

@Component({
  selector: 'app-convocatoriasinvest',
  templateUrl: './convocatoriasinvest.component.html',
  styleUrls: ['./convocatoriasinvest.component.css']
})
export class ConvocatoriasInvestComponent implements OnInit {
  currentUser: User | null = null;

  convocatorias: Convocatoria[];

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private authService: AuthService,
    private datePipe: DatePipe,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);

    this.cargarConvocatorias();

  }

  cargarConvocatorias(): void {
  
    this.convocatoriaService
        .getConvocatorias().subscribe({
          next: (response) => {
  
            const convocatoriasArr = Array.isArray(response)
              ? response
              : [];
  
            this.convocatorias = convocatoriasArr.map(p => {
  
              p.fechaInicio = p.fechaInicio
                ? this.datePipe.transform(
                    p.fechaInicio,
                    'dd/MM/yyyy'
                  ) || ''
                : '';
  
              p.fechaFin = p.fechaFin
                ? this.datePipe.transform(
                    p.fechaFin,
                    'dd/MM/yyyy'
                  ) || ''
                : '';
  
              return {
                ...p
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
                || 'Error al consultar convocatorias'
            });
          }
        });
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ROL_ADMIN');
  }

  isUser(): boolean {
    return this.authService.hasRole('ROL_USER');
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
