import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { CalificacionesEvalRoutingModule } from './calificacioneseval-routing.module';
import { CalificacionesEvalComponent } from './calificacioneseval.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        CalificacionesEvalRoutingModule, TooltipModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TagModule
    ],
    declarations: [CalificacionesEvalComponent],
    providers: [DatePipe]
})
export class CalificacionesEvalModule { }
