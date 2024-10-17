import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConvocatoriasInvestRoutingModule } from './convocatoriasinvest-routing.module';
import { ConvocatoriasInvestComponent } from './convocatoriasinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@NgModule({
    imports: [
        CommonModule,
        ConvocatoriasInvestRoutingModule,
        FormsModule, TableModule, ButtonModule
    ],
    declarations: [ConvocatoriasInvestComponent]
})
export class ConvocatoriasInvestModule { }
