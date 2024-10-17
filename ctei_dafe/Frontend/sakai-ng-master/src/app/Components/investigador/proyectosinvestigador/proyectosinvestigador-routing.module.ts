import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectosinvest', loadChildren: () => import('./proyectosinvest/proyectosinvest.module').then(m => m.ProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formproyectosinvest', loadChildren: () => import('./formproyectosinvest/formproyectosinvest.module').then(m => m.FormProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formproyectosinvest/:id', loadChildren: () => import('./formproyectosinvest/formproyectosinvest.module').then(m => m.FormProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formvistapreviaproyectosinvest', loadChildren: () => import('./formvistapreviaproyectosinvest/formvistapreviaproyectosinvest.module').then(m => m.FormVistaPreviaProyectosInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ProyectosInvestigadorRoutingModule { }
