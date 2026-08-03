import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'calificacionesinvest', loadChildren: () => import('./calificacionesinvest/calificacionesinvest.module').then(m => m.CalificacionesInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class CalificacionesInvestigadorRoutingModule { }
