import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'inscripcionesadmin', loadChildren: () => import('./inscripcionesadmin/inscripcionesadmin.module').then(m => m.InscripcionesAdminModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class InscripcionesAdministradorRoutingModule { }
