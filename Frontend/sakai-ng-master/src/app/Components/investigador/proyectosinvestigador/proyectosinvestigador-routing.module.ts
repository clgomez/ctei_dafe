import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosInvestigadorComponent } from './proyectosinvestigador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosInvestigadorComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosInvestigadorRoutingModule { }
