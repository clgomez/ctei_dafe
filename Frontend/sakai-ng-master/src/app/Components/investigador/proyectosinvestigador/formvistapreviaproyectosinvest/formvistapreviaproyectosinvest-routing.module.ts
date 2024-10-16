import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormVistaPreviaProyectosInvestComponent } from './formvistapreviaproyectosinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormVistaPreviaProyectosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class FormVistaPreviaProyectosInvestRoutingModule { }
