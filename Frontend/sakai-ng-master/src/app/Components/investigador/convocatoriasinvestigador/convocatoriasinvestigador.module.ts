import { NgModule } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ConvocatoriasInvestigadorRoutingModule } from './convocatoriasinvestigador-routing.module';

@NgModule({
    imports: [
        ConvocatoriasInvestigadorRoutingModule
    ],
    providers: [DatePipe]
})
export class ConvocatoriasInvestigadorModule { }
