import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TutorYEvaluadorAsignadosInvestComponent } from './tutoryevaluadorasignadosinvest.component';
@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: TutorYEvaluadorAsignadosInvestComponent }
    ])],
    exports: [RouterModule]
})
export class TutorYEvaluadorAsignadosInvestRoutingModule { }
