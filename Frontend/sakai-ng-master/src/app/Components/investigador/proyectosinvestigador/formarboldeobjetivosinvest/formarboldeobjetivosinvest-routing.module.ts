import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormArbolDeObjetivosInvestComponent } from './formarboldeobjetivosinvest.component';
@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormArbolDeObjetivosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class FormArbolDeObjetivosInvestRoutingModule { }
