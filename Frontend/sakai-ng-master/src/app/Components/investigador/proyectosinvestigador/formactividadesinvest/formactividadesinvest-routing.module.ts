import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormActividadesInvestComponent } from './formactividadesinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormActividadesInvestComponent }
    ])],
    exports: [RouterModule]
})
export class FormActividadesInvestRoutingModule { }
