import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InscripcionProyectoInvestRoutingModule } from './inscripcionproyectoinvest-routing.module';
import { InscripcionProyectoInvestComponent } from './inscripcionproyectoinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        InscripcionProyectoInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, TooltipModule
    ],
    declarations: [InscripcionProyectoInvestComponent]
})
export class InscripcionProyectoInvestModule { }
