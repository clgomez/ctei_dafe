import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InscripcionProyectoInvestComponent } from './inscripcionproyectoinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: InscripcionProyectoInvestComponent }
    ])],
    exports: [RouterModule]
})
export class InscripcionProyectoInvestRoutingModule { }
