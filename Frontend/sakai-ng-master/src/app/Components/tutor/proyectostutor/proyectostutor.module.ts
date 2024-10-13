import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosTutorRoutingModule } from './proyectostutor-routing.module';
import { ProyectosTutorComponent } from './proyectostutor.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosTutorRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosTutorComponent]
})
export class ProyectosTutorModule { }
