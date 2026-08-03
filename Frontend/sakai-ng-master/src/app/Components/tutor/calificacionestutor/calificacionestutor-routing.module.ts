import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'calificacionestut', loadChildren: () => import('./calificacionestut/calificacionestut.module').then(m => m.CalificacionesTutModule), canActivate: [AuthGuard] },
        { path: 'proyectostut/usuariotutor/:idusuariotutor', loadChildren: () => import('./proyectostut/proyectostut.module').then(m => m.ProyectosTutModule), canActivate: [AuthGuard] },
        { path: 'formcalificaciontut/proyecto/:idproyecto/usuariotutor/:idusuariotutor', loadChildren: () => import('./formcalificaciontut/formcalificaciontut.module').then(m => m.FormCalificacionTutModule), canActivate: [AuthGuard] },
        { path: 'formcalificaciontut/calificacion/:idcalificacion', loadChildren: () => import('./formcalificaciontut/formcalificaciontut.module').then(m => m.FormCalificacionTutModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }

    ])],
    exports: [RouterModule]
})
export class CalificacionesTutorRoutingModule { }
