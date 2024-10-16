import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeInvestigadorComponent } from './homeinvestigador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: HomeInvestigadorComponent }
    ])],
    exports: [RouterModule]
})
export class HomeInvestigadorRoutingModule { }
