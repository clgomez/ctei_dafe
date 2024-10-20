import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'inscripcionproyectoinvest/:id', loadChildren: () => import('./inscripcionproyectoinvest/inscripcionproyectoinvest.module').then(m => m.InscripcionProyectoInvestModule), canActivate: [AuthGuard] },
        { path: 'inscripcionesinvest', loadChildren: () => import('./inscripcionesinvest/inscripcionesinvest.module').then(m => m.InscripcionesInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class InscripcionesInvestigadorRoutingModule { }
