import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormProyectosInvestComponent } from './formproyectosinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormProyectosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class FormProyectosInvestRoutingModule { }
