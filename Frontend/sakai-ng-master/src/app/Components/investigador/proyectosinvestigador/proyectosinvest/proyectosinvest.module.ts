import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosInvestRoutingModule } from './proyectosinvest-routing.module';
import { ProyectosInvestComponent } from './proyectosinvest.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosInvestRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosInvestComponent]
})
export class ProyectosInvestModule { }
