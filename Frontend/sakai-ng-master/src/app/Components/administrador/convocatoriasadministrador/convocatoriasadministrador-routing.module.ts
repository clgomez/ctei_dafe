import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'convocatoriasadmin', loadChildren: () => import('./convocatoriasadmin/convocatoriasadmin.module').then(m => m.ConvocatoriasAdminModule), canActivate: [AuthGuard] },
        { path: 'formconvocatoriasadmin', loadChildren: () => import('./formconvocatoriasadmin/formconvocatoriasadmin.module').then(m => m.FormConvocatoriasAdminModule), canActivate: [AuthGuard] },
        { path: 'formconvocatoriasadmin/:id', loadChildren: () => import('./formconvocatoriasadmin/formconvocatoriasadmin.module').then(m => m.FormConvocatoriasAdminModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasAdministradorRoutingModule { }
