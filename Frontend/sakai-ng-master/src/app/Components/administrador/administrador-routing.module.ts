import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'convocatoriasadministrador', loadChildren: () => import('./convocatoriasadministrador/convocatoriasadministrador.module').then(m => m.ConvocatoriasAdministradorModule), canActivate: [AuthGuard] },
        { path: 'proyectosadministrador', loadChildren: () => import('./proyectosadministrador/proyectosadministrador.module').then(m => m.ProyectosAdministradorModule), canActivate: [AuthGuard] },
        { path: 'asignacionderolesadministrador', loadChildren: () => import('./asignacionderolesadministrador/asignacionderolesadministrador.module').then(m => m.AsignacionDeRolesAdministradorModule), canActivate: [AuthGuard] },
        { path: 'inscripcionesadministrador', loadChildren: () => import('./inscripcionesadministrador/inscripcionesadministrador.module').then(m => m.InscripcionesAdministradorModule), canActivate: [AuthGuard] },
        { path: 'usuariosadministrador', loadChildren: () => import('./usuariosadministrador/usuariosadministrador.module').then(m => m.UsuariosAdministradorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class AdministradorRoutingModule { }
