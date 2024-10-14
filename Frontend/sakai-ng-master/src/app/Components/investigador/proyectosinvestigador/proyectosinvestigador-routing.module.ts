import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectosinvest', loadChildren: () => import('./proyectosinvest/proyectosinvest.module').then(m => m.ProyectosInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ProyectosInvestigadorRoutingModule { }
