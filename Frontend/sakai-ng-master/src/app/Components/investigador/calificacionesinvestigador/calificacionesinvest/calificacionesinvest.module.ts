import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { CalificacionesInvestRoutingModule } from './calificacionesinvest-routing.module';
import { CalificacionesInvestComponent } from './calificacionesinvest.component'; 
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        CalificacionesInvestRoutingModule, TooltipModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TagModule
    ],
    declarations: [CalificacionesInvestComponent],
    providers: [DatePipe]
})
export class CalificacionesInvestModule { }