import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'proyectostut', loadChildren: () => import('./proyectostut/proyectostut.module').then(m => m.ProyectosTutModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class ProyectosTutorRoutingModule { }
