import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { CalificacionesTutRoutingModule } from './calificacionestut-routing.module';
import { CalificacionesTutComponent } from './calificacionestut.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        CalificacionesTutRoutingModule, TooltipModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TagModule
    ],
    declarations: [CalificacionesTutComponent],
    providers: [DatePipe]
})
export class CalificacionesTutModule { }
