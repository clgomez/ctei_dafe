import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConvocatoriasInvestigadorRoutingModule } from './convocatoriasinvestigador-routing.module';
import { ConvocatoriasInvestigadorComponent } from './convocatoriasinvestigador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ConvocatoriasInvestigadorRoutingModule,
        FormsModule
    ],
    declarations: [ConvocatoriasInvestigadorComponent]
})
export class ConvocatoriasInvestigadorModule { }
