import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosTutRoutingModule } from './proyectostut-routing.module';
import { ProyectosTutComponent } from './proyectostut.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosTutRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosTutComponent]
})
export class ProyectosTutModule { }
