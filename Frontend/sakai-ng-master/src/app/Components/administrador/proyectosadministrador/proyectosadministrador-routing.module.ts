import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectosadmin', loadChildren: () => import('./proyectosadmin/proyectosadmin.module').then(m => m.ProyectosAdminModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ProyectosAdministradorRoutingModule { }
