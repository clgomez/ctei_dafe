import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'hometutor', loadChildren: () => import('./hometutor/hometutor.module').then(m => m.HomeTutorModule), canActivate: [AuthGuard] },
        { path: 'proyectostutor', loadChildren: () => import('./proyectostutor/proyectostutor.module').then(m => m.ProyectosTutorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class TutorRoutingModule { }
