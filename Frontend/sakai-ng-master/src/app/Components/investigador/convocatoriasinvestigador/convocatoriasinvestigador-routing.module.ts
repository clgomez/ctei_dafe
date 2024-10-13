import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConvocatoriasInvestigadorComponent } from './convocatoriasinvestigador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ConvocatoriasInvestigadorComponent }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasInvestigadorRoutingModule { }
