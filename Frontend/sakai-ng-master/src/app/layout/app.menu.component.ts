import { ChangeDetectorRef, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { AuthService } from '../Services/auth.service';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html'
})
export class AppMenuComponent implements OnInit {

    model: any[] = [];

    constructor(public layoutService: LayoutService,
                private authService: AuthService,
                private cd: ChangeDetectorRef ) { }

    ngOnInit() {
        this.model = [
            {
                label: 'Home',
                items: [
                    { label: 'Página Principal', icon: 'pi pi-fw pi-home', routerLink: ['/welcome'] }
                ]
            }
        ];

        // Escuchar cambios de autenticación (por ejemplo, con un observable)
        this.authService.roleChanges().subscribe((role: string) => {
            console.log(role);
            this.updateMenu(role);
        });

        // Verificar el rol actual del usuario
        //const currentRole = this.authService.getCurrentRole();
        //console.log(currentRole);
        //this.updateMenu(currentRole);

    }

    updateMenu(role: string) {
        // Limpiar las herramientas de investigador si ya existen
        this.model = this.model.filter(item => item.label !== 'Herramientas de Usuario Investigador');

        // Agregar las herramientas si el usuario es investigador
        if (role === 'ROL_INVESTIGADOR') {
            this.model.push({
                label: 'Herramientas de Usuario Investigador',
                items: [
                    { label: 'Notificaciones', icon: 'pi pi-fw pi-bell', routerLink: ['/investigador/notificacionesinvestigador/notificacionesinvest'] },
                    { label: 'Convocatorias', icon: 'pi pi-fw pi-megaphone', routerLink: ['/investigador/convocatoriasinvestigador/convocatoriasinvest'] },
                    { label: 'Proyectos', icon: 'pi pi-fw pi-lightbulb', routerLink: ['/investigador/proyectosinvestigador/proyectosinvest'] },
                    { label: 'Inscripciones', icon: 'pi pi-fw pi-book', routerLink: ['/investigador/inscripcionesinvestigador/inscripcionesinvest'] }
                ]
            });
        }

        // Limpiar las herramientas de tutor si ya existen
        this.model = this.model.filter(item => item.label !== 'Herramientas de Usuario Tutor');

        // Agregar las herramientas si el usuario es tutor
        if (role === 'ROL_TUTOR') {
            this.model.push({
                label: 'Herramientas de Usuario Tutor',
                items: [
                    { label: 'Proyectos', icon: 'pi pi-fw pi-book', routerLink: ['/tutor/proyectostutor/proyectostut'] }
                ]
            });
        }

        // Limpiar las herramientas de evaluador si ya existen
        this.model = this.model.filter(item => item.label !== 'Herramientas de Usuario Evaluador');

        // Agregar las herramientas si el usuario es evaluador
        if (role === 'ROL_EVALUADOR') {
            this.model.push({
                label: 'Herramientas de Usuario Evaluador',
                items: [
                    { label: 'Proyectos', icon: 'pi pi-fw pi-lightbulb', routerLink: ['/evaluador/proyectosevaluador/proyectoseval'] }
                ]
            });
        }

        // Limpiar las herramientas de administrador si ya existen
        this.model = this.model.filter(item => item.label !== 'Herramientas de Usuario Administrador');

        // Agregar las herramientas si el usuario es administrador
        if (role === 'ROL_ADMINISTRADOR') {
            this.model.push({
                label: 'Herramientas de Usuario Administrador',
                items: [
                    { label: 'Convocatorias', icon: 'pi pi-fw pi-megaphone', routerLink: ['/administrador/convocatoriasadministrador/convocatoriasadmin'] },
                    { label: 'Proyectos', icon: 'pi pi-fw pi-lightbulb', routerLink: ['/administrador/proyectosadministrador/proyectosadmin'] },
                    { label: 'Inscripciones', icon: 'pi pi-fw pi-book', routerLink: ['/administrador/inscripcionesadministrador/inscripcionesadmin'] },
                    { label: 'Asignación de Roles', icon: 'pi pi-fw pi-user-plus', routerLink: ['/administrador/asignacionderolesadministrador/asignacionderolesadmin'] }
                ]
            });
        }

        // Forzar la detección de cambios para actualizar la vista del sidebar
        this.cd.detectChanges();
    }
}
