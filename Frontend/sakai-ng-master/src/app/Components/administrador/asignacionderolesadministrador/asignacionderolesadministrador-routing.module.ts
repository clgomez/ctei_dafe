import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'asignacionderolesadmin', loadChildren: () => import('./asignacionderolesadmin/asignacionderolesadmin.module').then(m => m.AsignacionDeRolesAdminModule), canActivate: [AuthGuard] },
        { path: 'formasignacionderolesadmin/:id', loadChildren: () => import('./formasignacionderolesadmin/formasignacionderolesadmin.module').then(m => m.FormAsignacionDeRolesAdminModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class AsignacionDeRolesAdministradorRoutingModule { }

