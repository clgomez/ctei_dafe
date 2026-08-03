import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'calificacioneseval', loadChildren: () => import('./calificacioneseval/calificacioneseval.module').then(m => m.CalificacionesEvalModule), canActivate: [AuthGuard] },
        { path: 'proyectoseval/usuarioevaluador/:idusuarioevaluador', loadChildren: () => import('./proyectoseval/proyectoseval.module').then(m => m.ProyectosEvalModule), canActivate: [AuthGuard] },
        { path: 'formcalificacioneval/proyecto/:idproyecto/usuarioevaluador/:idusuarioevaluador', loadChildren: () => import('./formcalificacioneval/formcalificacioneval.module').then(m => m.FormCalificacionEvalModule), canActivate: [AuthGuard] },
        { path: 'formcalificacioneval/calificacion/:idcalificacion', loadChildren: () => import('./formcalificacioneval/formcalificacioneval.module').then(m => m.FormCalificacionEvalModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }

    ])],
    exports: [RouterModule]
})
export class CalificacionesEvaluadorRoutingModule { }
