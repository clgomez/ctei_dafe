import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InscripcionesInvestRoutingModule } from './inscripcionesinvest-routing.module';
import { InscripcionesInvestComponent } from './inscripcionesinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@NgModule({
    imports: [
        CommonModule,
        InscripcionesInvestRoutingModule,
        FormsModule, TableModule, ButtonModule
    ],
    declarations: [InscripcionesInvestComponent]
})
export class InscripcionesInvestModule { }
