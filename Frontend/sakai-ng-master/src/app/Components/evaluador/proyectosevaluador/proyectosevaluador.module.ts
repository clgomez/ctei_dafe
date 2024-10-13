import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosEvaluadorRoutingModule } from './proyectosevaluador-routing.module';
import { ProyectosEvaluadorComponent } from './proyectosevaluador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosEvaluadorRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosEvaluadorComponent]
})
export class ProyectosEvaluadorModule { }
