import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'tutoryevaluadorasignadosinvest', loadChildren: () => import('./tutoryevaluadorasignadosinvest/tutoryevaluadorasignadosinvest.module').then(m => m.TutorYEvaluadorAsignadosInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class AsignacionDeRolesInvestigadorRoutingModule { }
