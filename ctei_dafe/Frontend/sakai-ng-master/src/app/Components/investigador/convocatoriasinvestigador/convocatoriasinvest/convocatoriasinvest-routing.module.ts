import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConvocatoriasInvestComponent } from './convocatoriasinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ConvocatoriasInvestComponent }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasInvestRoutingModule { }
