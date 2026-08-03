import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosInvestComponent } from './proyectosinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosInvestRoutingModule { }
