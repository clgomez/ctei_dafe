import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActividadesInvestComponent } from './actividadesinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ActividadesInvestComponent }
    ])],
    exports: [RouterModule]
})
export class ActividadesInvestRoutingModule { }
