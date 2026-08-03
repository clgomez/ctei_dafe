import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArbolDeObjetivosInvestComponent } from './arboldeobjetivosinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ArbolDeObjetivosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class ArbolDeObjetivosInvestRoutingModule { }