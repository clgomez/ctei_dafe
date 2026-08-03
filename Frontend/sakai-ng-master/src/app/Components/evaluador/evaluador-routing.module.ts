import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        
        { path: 'calificacionesevaluador', loadChildren: () => import('./calificacionesevaluador/calificacionesevaluador.module').then(m => m.CalificacionesEvaluadorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class EvaluadorRoutingModule { }
