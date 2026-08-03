import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ProyectosTutRoutingModule } from './proyectostut-routing.module';
import { ProyectosTutComponent } from './proyectostut.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ProyectosTutRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ProyectosTutComponent],
    providers: [DatePipe]
})
export class ProyectosTutModule { }
