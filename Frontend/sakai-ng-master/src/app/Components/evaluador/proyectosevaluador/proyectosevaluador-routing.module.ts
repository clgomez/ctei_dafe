import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosEvaluadorComponent } from './proyectosevaluador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosEvaluadorComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosEvaluadorRoutingModule { }
