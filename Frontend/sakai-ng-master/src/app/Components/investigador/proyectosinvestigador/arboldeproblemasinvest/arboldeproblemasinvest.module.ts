import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArbolDeProblemasInvestRoutingModule } from './arboldeproblemasinvest-routing.module';
import { ArbolDeProblemasInvestComponent } from './arboldeproblemasinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ArbolDeProblemasInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ArbolDeProblemasInvestComponent]
})
export class ArbolDeProblemasInvestModule { }
