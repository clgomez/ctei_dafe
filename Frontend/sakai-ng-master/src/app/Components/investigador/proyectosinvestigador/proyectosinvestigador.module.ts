import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosInvestigadorRoutingModule } from './proyectosinvestigador-routing.module';
import { ProyectosInvestigadorComponent } from './proyectosinvestigador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosInvestigadorRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosInvestigadorComponent]
})
export class ProyectosInvestigadorModule { }
