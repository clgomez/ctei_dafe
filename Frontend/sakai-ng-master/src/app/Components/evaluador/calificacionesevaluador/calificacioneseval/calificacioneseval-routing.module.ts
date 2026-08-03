import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CalificacionesEvalComponent } from './calificacioneseval.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: CalificacionesEvalComponent }
    ])],
    exports: [RouterModule]
})
export class CalificacionesEvalRoutingModule { }
