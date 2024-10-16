import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosInvestRoutingModule } from './proyectosinvest-routing.module';
import { ProyectosInvestComponent } from './proyectosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

@NgModule({
    imports: [
        CommonModule,
        ProyectosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule
    ],
    declarations: [ProyectosInvestComponent]
})
export class ProyectosInvestModule { }
