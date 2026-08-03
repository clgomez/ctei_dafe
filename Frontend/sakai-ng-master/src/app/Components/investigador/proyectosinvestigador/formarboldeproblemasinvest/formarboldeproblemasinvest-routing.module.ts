import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormArbolDeProblemasInvestComponent } from './formarboldeproblemasinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormArbolDeProblemasInvestComponent }
    ])],
    exports: [RouterModule]
})
export class FormArbolDeProblemasInvestRoutingModule { }
