import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosTutComponent } from './proyectostut.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosTutComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosTutRoutingModule { }
