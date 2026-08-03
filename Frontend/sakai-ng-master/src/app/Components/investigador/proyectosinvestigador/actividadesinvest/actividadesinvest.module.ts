import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActividadesInvestRoutingModule } from './actividadesinvest-routing.module';
import { ActividadesInvestComponent } from './actividadesinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ActividadesInvestRoutingModule,
        FormsModule, TableModule, ButtonModule,DialogModule, TooltipModule
    ],
    declarations: [ActividadesInvestComponent]
})
export class ActividadesInvestModule { }
