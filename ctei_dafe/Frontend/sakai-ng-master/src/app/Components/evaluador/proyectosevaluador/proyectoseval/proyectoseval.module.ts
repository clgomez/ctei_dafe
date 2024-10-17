import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosEvalRoutingModule } from './proyectoseval-routing.module';
import { ProyectosEvalComponent } from './proyectoseval.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosEvalRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosEvalComponent]
})
export class ProyectosEvalModule { }
