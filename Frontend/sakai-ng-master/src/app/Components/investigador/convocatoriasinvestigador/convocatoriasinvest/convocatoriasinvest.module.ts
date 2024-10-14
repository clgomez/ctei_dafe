import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConvocatoriasInvestRoutingModule } from './convocatoriasinvest-routing.module';
import { ConvocatoriasInvestComponent } from './convocatoriasinvest.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ConvocatoriasInvestRoutingModule,
        FormsModule
    ],
    declarations: [ConvocatoriasInvestComponent]
})
export class ConvocatoriasInvestModule { }
