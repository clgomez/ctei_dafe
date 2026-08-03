import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'usuariosadmin', loadChildren: () => import('./usuariosadmin/usuariosadmin.module').then(m => m.UsuariosAdminModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class UsuariosAdministradorRoutingModule { }
