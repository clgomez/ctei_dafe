import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ProyectosInvestRoutingModule } from './proyectosinvest-routing.module';
import { ProyectosInvestComponent } from './proyectosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { SplitButtonModule } from 'primeng/splitbutton';

@NgModule({
    imports: [
        CommonModule,
        ProyectosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule, SplitButtonModule
    ],
    declarations: [ProyectosInvestComponent],
    providers: [DatePipe]
})
export class ProyectosInvestModule { }
