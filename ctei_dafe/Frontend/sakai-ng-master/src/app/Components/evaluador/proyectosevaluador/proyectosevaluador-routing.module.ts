import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectoseval', loadChildren: () => import('./proyectoseval/proyectoseval.module').then(m => m.ProyectosEvalModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ProyectosEvaluadorRoutingModule { }
