import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeInvestigadorRoutingModule } from './homeinvestigador-routing.module';
import { HomeInvestigadorComponent } from './homeinvestigador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        HomeInvestigadorRoutingModule,
        FormsModule
    ],
    declarations: [HomeInvestigadorComponent]
})
export class HomeInvestigadorModule { }
