import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArbolDeObjetivosInvestRoutingModule } from './arboldeobjetivosinvest-routing.module';
import { ArbolDeObjetivosInvestComponent } from './arboldeobjetivosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ArbolDeObjetivosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ArbolDeObjetivosInvestComponent]
})
export class ArbolDeObjetivosInvestModule { }
