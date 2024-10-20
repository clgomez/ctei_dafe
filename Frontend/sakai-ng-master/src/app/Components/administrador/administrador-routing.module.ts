import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'homeadministrador', loadChildren: () => import('./homeadministrador/homeadministrador.module').then(m => m.HomeAdministradorModule), canActivate: [AuthGuard] },
        { path: 'convocatoriasadministrador', loadChildren: () => import('./convocatoriasadministrador/convocatoriasadministrador.module').then(m => m.ConvocatoriasAdministradorModule), canActivate: [AuthGuard] },
        { path: 'proyectosadministrador', loadChildren: () => import('./proyectosadministrador/proyectosadministrador.module').then(m => m.ProyectosAdministradorModule), canActivate: [AuthGuard] },
        { path: 'asignacionderolesadministrador', loadChildren: () => import('./asignacionderolesadministrador/asignacionderolesadministrador.module').then(m => m.AsignacionDeRolesAdministradorModule), canActivate: [AuthGuard] },
        { path: 'inscripcionesadministrador', loadChildren: () => import('./inscripcionesadministrador/inscripcionesadministrador.module').then(m => m.InscripcionesAdministradorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class AdministradorRoutingModule { }
