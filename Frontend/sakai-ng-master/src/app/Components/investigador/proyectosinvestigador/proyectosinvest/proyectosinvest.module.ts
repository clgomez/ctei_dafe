import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosInvestRoutingModule } from './proyectosinvest-routing.module';
import { ProyectosInvestComponent } from './proyectosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ProyectosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ProyectosInvestComponent]
})
export class ProyectosInvestModule { }
