import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ProyectosEvalRoutingModule } from './proyectoseval-routing.module';
import { ProyectosEvalComponent } from './proyectoseval.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ProyectosEvalRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ProyectosEvalComponent],
    providers: [DatePipe]
})
export class ProyectosEvalModule { }
