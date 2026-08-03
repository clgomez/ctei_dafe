import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosEvalComponent } from './proyectoseval.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosEvalComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosEvalRoutingModule { }
