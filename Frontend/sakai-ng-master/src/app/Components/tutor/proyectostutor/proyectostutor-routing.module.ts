import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosTutorComponent } from './proyectostutor.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosTutorComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosTutorRoutingModule { }
