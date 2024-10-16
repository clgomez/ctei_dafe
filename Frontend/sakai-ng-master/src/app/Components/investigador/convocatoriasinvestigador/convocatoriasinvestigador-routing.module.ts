import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'convocatoriasinvest', loadChildren: () => import('./convocatoriasinvest/convocatoriasinvest.module').then(m => m.ConvocatoriasInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasInvestigadorRoutingModule { }
