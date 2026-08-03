import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CalificacionesTutComponent } from './calificacionestut.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: CalificacionesTutComponent }
    ])],
    exports: [RouterModule]
})
export class CalificacionesTutRoutingModule { }
