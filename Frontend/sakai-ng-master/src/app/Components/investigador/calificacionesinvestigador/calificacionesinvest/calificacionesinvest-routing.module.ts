import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CalificacionesInvestComponent } from './calificacionesinvest.component'; 

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: CalificacionesInvestComponent }
    ])],
    exports: [RouterModule]
})
export class CalificacionesInvestRoutingModule { }