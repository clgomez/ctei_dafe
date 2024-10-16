import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'homeevaluador', loadChildren: () => import('./homeevaluador/homeevaluador.module').then(m => m.HomeEvaluadorModule), canActivate: [AuthGuard] },
        { path: 'proyectosevaluador', loadChildren: () => import('./proyectosevaluador/proyectosevaluador.module').then(m => m.ProyectosEvaluadorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class EvaluadorRoutingModule { }
