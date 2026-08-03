import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        
        { path: 'calificacionestutor', loadChildren: () => import('./calificacionestutor/calificacionestutor.module').then(m => m.CalificacionesTutorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class TutorRoutingModule { }
