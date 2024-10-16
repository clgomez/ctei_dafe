import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeEvaluadorComponent } from './homeevaluador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: HomeEvaluadorComponent }
    ])],
    exports: [RouterModule]
})
export class HomeEvaluadorRoutingModule { }
