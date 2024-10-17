import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacionesInvestRoutingModule } from './notificacionesinvest-routing.module';
import { NotificacionesInvestComponent } from './notificacionesinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        NotificacionesInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, TooltipModule
    ],
    declarations: [NotificacionesInvestComponent]
})
export class NotificacionesInvestModule { }
