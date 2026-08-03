import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectosinvest', loadChildren: () => import('./proyectosinvest/proyectosinvest.module').then(m => m.ProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formproyectosinvest', loadChildren: () => import('./formproyectosinvest/formproyectosinvest.module').then(m => m.FormProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formproyectosinvest/:id', loadChildren: () => import('./formproyectosinvest/formproyectosinvest.module').then(m => m.FormProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formvistapreviaproyectosinvest', loadChildren: () => import('./formvistapreviaproyectosinvest/formvistapreviaproyectosinvest.module').then(m => m.FormVistaPreviaProyectosInvestModule), canActivate: [AuthGuard] },
        { path: 'formarboldeproblemasinvest/:idarbol', loadChildren: () => import('./formarboldeproblemasinvest/formarboldeproblemasinvest.module').then(m => m.FormArbolDeProblemasInvestModule), canActivate: [AuthGuard] },
        { path: 'formarboldeproblemasinvest/proyecto/:idproyecto', loadChildren: () => import('./formarboldeproblemasinvest/formarboldeproblemasinvest.module').then(m => m.FormArbolDeProblemasInvestModule), canActivate: [AuthGuard] },
        { path: 'formarboldeobjetivosinvest/:idarbol', loadChildren: () => import('./formarboldeobjetivosinvest/formarboldeobjetivosinvest.module').then(m => m.FormArbolDeObjetivosInvestModule), canActivate: [AuthGuard] },
        { path: 'formarboldeobjetivosinvest/proyecto/:idproyecto', loadChildren: () => import('./formarboldeobjetivosinvest/formarboldeobjetivosinvest.module').then(m => m.FormArbolDeObjetivosInvestModule), canActivate: [AuthGuard] },
        { path: 'formactividadesinvest/:id', loadChildren: () => import('./formactividadesinvest/formactividadesinvest.module').then(m => m.FormActividadesInvestModule), canActivate: [AuthGuard] },
        { path: 'actividadesinvest/:id', loadChildren: () => import('./actividadesinvest/actividadesinvest.module').then(m => m.ActividadesInvestModule), canActivate: [AuthGuard] },
        { path: 'arboldeproblemasinvest/:id', loadChildren: () => import('./arboldeproblemasinvest/arboldeproblemasinvest.module').then(m => m.ArbolDeProblemasInvestModule), canActivate: [AuthGuard] },
        { path: 'arboldeobjetivosinvest/:id', loadChildren: () => import('./arboldeobjetivosinvest/arboldeobjetivosinvest.module').then(m => m.ArbolDeObjetivosInvestModule), canActivate: [AuthGuard] },
        { path: 'formactividadesinvest/:id', loadChildren: () => import('./formactividadesinvest/formactividadesinvest.module').then(m => m.FormActividadesInvestModule), canActivate: [AuthGuard] },
        { path: 'actividadesinvest/:id', loadChildren: () => import('./actividadesinvest/actividadesinvest.module').then(m => m.ActividadesInvestModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }

    ])],
    exports: [RouterModule]
})
export class ProyectosInvestigadorRoutingModule { }
