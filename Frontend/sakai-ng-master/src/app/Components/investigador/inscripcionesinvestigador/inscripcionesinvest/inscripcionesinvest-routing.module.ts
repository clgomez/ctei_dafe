import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InscripcionesInvestComponent } from './inscripcionesinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: InscripcionesInvestComponent }
    ])],
    exports: [RouterModule]
})
export class InscripcionesInvestRoutingModule { }
