import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'notificacionesinvest', loadChildren: () => import('./notificacionesinvest/notificacionesinvest.module').then(m => m.NotificacionesInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class NotificacionesInvestigadorRoutingModule { }
