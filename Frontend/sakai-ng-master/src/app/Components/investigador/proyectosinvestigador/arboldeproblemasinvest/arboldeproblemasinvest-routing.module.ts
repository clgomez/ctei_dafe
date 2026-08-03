import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArbolDeProblemasInvestComponent } from './arboldeproblemasinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ArbolDeProblemasInvestComponent }
    ])],
    exports: [RouterModule]
})
export class ArbolDeProblemasInvestRoutingModule { }